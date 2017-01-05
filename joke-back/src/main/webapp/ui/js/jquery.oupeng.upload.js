/**
 *
 * option参数:
 * data - 随文件提交参数
 * url - 上传地址
 * acceptFileTypes － 文件类型，MIME
 * maxFileSize - 上传最大限制(单位字节)
 * minFileSize - 上传最小限制(单位字节)
 * onUploadSuccess - 上传成功回调
 * onUploadError - 校验失败或者上传失败回调
 */
(function ($) {
    var uuid = 0;

    $.fn.OupengUpload = function (file, options) {

        if (!validate(file, options)) {
            return;
        }

        var self = this, inputs, checkbox, checked,
            iframeName = 'jquery_upload' + ++uuid,
            iframe = $('<iframe name="' + iframeName + '" style="position:absolute;top:-9999px" />').appendTo('body'),
            form = '<form target="' + iframeName + '" method="post" enctype="multipart/form-data" />';

        checkbox = $('input:checkbox', this);
        checked = $('input:checked', this);
        form = self.wrapAll(form).parent('form').attr('action', options.url);

        checkbox.removeAttr('checked');
        checked.attr('checked', true);

        inputs = createInputs(options.data);
        inputs = inputs ? $(inputs).appendTo(form) : null;

        form.submit(function () {
            iframe.load(function () {
                var result = handleData(this),
                    checked = $('input:checked', self);

                form.after(self).remove();
                checkbox.removeAttr('checked');
                checked.attr('checked', true);
                if (inputs) {
                    inputs.remove();
                }

                if (result.status == 1) {
                    if ($.type(options.onUploadSuccess) === 'function') {
                        options.onUploadSuccess(result.info);
                        return;
                    }
                } else {
                    options.onUploadError(result.info);
                }

                setTimeout(function () {
                    iframe.remove();
                }, 0);
            });
        }).submit();

        return this;
    };


    function validate(file, options) {

        var fileSize;
        if (options.minFileSize || options.maxFileSize) {
            fileSize = file.size;
        }
        if ((options.acceptFileTypes != "") && (options.acceptFileTypes != '*') && !file.type.match(options.acceptFileTypes)) {
            options.onUploadError('不符合的文件类型');
            return false;
        } else if (options.maxFileSize != "" && fileSize > options.maxFileSize) {
            options.onUploadError('超过最大限制');
            return false;
        } else if ($.type(fileSize) === 'number' &&
            fileSize < options.minFileSize) {
            options.onUploadError('低于最小限制');
            return false;
        }
        return true;
    }

    function createInputs(data) {
        return $.map(param(data), function (param) {
            var e = $(document.createElement('input'));
            e.attr('type', 'hidden');
            e.attr('name', param.name);
            e.attr('value', encodeURI(param.value));
            return e;
        });
    }

    function param(data) {
        if ($.isArray(data)) {
            return data;
        }
        var params = [];

        function add(name, value) {
            params.push({name: name, value: value});
        }

        if (typeof data === 'object') {
            $.each(data, function (name) {
                if ($.isArray(this)) {
                    $.each(this, function () {
                        add(name, this);
                    });
                } else {
                    add(name, $.isFunction(this) ? this() : this);
                }
            });
        } else if (typeof data === 'string') {
            $.each(data.split('&'), function () {
                var param = $.map(this.split('='), function (v) {
                    return decodeURIComponent(v.replace(/\+/g, ' '));
                });
                add(param[0], param[1]);
            });
        }

        return params;
    }

    function handleData(iframe) {
        var data, contents = $(iframe).contents().get(0);
        data = $(contents).find('body').text();
        data = $.parseJSON(data);
        return data;
    }

})(jQuery);
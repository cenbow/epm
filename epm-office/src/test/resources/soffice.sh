# Install libreoffice-headless
unset DISPLAY
soffice -accept="socket,host=127.0.0.1,port=8100;urp;StarOffice.ServiceManager" \
        --nofirststartwizard \
        --headless \
        --nocrashreport \
        --nodefault \
        --nolockcheck \
        --nologo \
        --norestore
package com.oni.training.springboot.Ch24.util;

import java.beans.PropertyEditorSupport;

public class ToSearchTextEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
//        super.setAsText(text);
        var t=CommonUtil.toSearchText(text);
        super.setValue(t);
    }
}

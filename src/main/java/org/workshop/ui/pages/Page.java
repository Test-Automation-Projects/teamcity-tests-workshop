package org.workshop.ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.element;

public abstract class Page {
    public abstract Page open();

    protected SelenideElement proceedButton = element("input[value='proceed']");
}

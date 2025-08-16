package com.generoso.ft.identity;

public class Main {

    public static void main(String[] args) {
        var testArgs = new String[]{
            "--tags", "not @ignore",
            "--plugin", "pretty",
            "--plugin", "html:ft/reports/cucumber-report.html",
            "--glue", "com.generoso.ft.identity",
            "classpath:features"
        };

        io.cucumber.core.cli.Main.main(testArgs);
    }
}

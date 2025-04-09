package com.roger.javamodule.collect_string.feedback;

import java.util.List;

public class ReportMsg {
    protected int id;
    protected List<ReportLog> report_options;
    protected List<ReportConfig> report_config;
    protected String lang;
    protected String code_url;
    protected String code_explain;
    protected String prompt_message;
    protected List<ReportLog> report_options_subtitle;
    protected List<ReportLog> report_options_playback;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReportLog> getReport_options() {
        return report_options;
    }

    public void setReport_options(List<ReportLog> report_options) {
        this.report_options = report_options;
    }

    public List<ReportConfig> getReport_config() {
        return report_config;
    }

    public void setReport_config(List<ReportConfig> report_config) {
        this.report_config = report_config;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getCode_explain() {
        return code_explain;
    }

    public void setCode_explain(String code_explain) {
        this.code_explain = code_explain;
    }

    public String getPrompt_message() {
        return prompt_message;
    }

    public void setPrompt_message(String prompt_message) {
        this.prompt_message = prompt_message;
    }

    public void setReport_options_subtitle(List<ReportLog> report_options_subtitle) {
        this.report_options_subtitle = report_options_subtitle;
    }

    public List<ReportLog> getReport_options_subtitle() {
        return this.report_options_subtitle;
    }

    public void setReport_options_playback(List<ReportLog> report_options_playback) {
        this.report_options_playback = report_options_playback;
    }

    public List<ReportLog> getReport_options_playback() {
        return this.report_options_playback;
    }
}

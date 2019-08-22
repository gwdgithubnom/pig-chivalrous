package org.gjgr.pig.chivalrous.mail;

import org.gjgr.pig.chivalrous.core.entity.Message;

/**
 * @Author gwd
 * @Time 01-31-2019 Thursday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class SimpleMail {

    private String copyright;
    private String more;
    private String contact;
    private String title;
    private String content;
    private String author;
    private String from;
    private String to;
    private String cc;
    private String file;
    private String message;
    private String url;
    private String nickname;
    private String headline;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message format(Message message) {
        MailCommand.formatMessage(message, this);
        MailCommand.formatMessage(message);
        return message;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCopyright() {
        return copyright;
    }

    public SimpleMail setCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    public String getMore() {
        return more;
    }

    public SimpleMail setMore(String more) {
        this.more = more;
        return this;

    }

    public String getContact() {
        return contact;
    }

    public SimpleMail setContact(String contact) {
        this.contact = contact;
        return this;

    }

    public String getTitle() {
        return title;
    }

    public SimpleMail setTitle(String title) {
        this.title = title;
        return this;

    }

    public String getContent() {
        return content;
    }

    public SimpleMail setContent(String content) {
        this.content = content;
        return this;

    }

    public String getAuthor() {
        return author;
    }

    public SimpleMail setAuthor(String author) {
        this.author = author;
        return this;

    }

    public String getFrom() {
        return from;
    }

    public SimpleMail setFrom(String from) {
        this.from = from;
        return this;

    }

    public String getTo() {
        return to;
    }

    public SimpleMail setTo(String to) {
        this.to = to;
        return this;

    }

    public String getCc() {
        return cc;
    }

    public SimpleMail setCc(String cc) {
        this.cc = cc;
        return this;

    }

    public String getFile() {
        return file;
    }

    public SimpleMail setFile(String file) {
        this.file = file;
        return this;

    }

    public String getMessage() {
        return message;
    }

    public SimpleMail setMessage(String message) {
        this.message = message;
        return this;

    }

    public String getUrl() {
        return url;
    }

    public SimpleMail setUrl(String url) {
        this.url = url;
        return this;

    }

    public String getNickname() {
        return nickname;
    }

    public SimpleMail setNickname(String nickname) {
        this.nickname = nickname;
        return this;

    }
}

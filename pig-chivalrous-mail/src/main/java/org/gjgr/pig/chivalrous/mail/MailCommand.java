package org.gjgr.pig.chivalrous.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.http.NameValuePair;
import org.gjgr.pig.chivalrous.core.Command;
import org.gjgr.pig.chivalrous.core.entity.Message;
import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.json.bean.MapJson;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.Nullable;
import org.gjgr.pig.chivalrous.core.net.UriBuilder;
import org.gjgr.pig.chivalrous.core.net.UriCommand;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @Author gwd
 * @Time 01-31-2019 Thursday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public final class MailCommand {
    private static final String row = "style=\"vertical-align: top; padding: 0;\" align=\"left\"";
    private static final String column =
            "class=\"center\" style=\"word-break: break-word; -webkit-hyphens: auto; -moz-hyphens: auto; hyphens: auto; border-collapse: collapse !important; color: #222222; font-family: 'Open Sans', 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-weight: normal; line-height: 19px; font-size: 14px; -webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; margin: 0; padding: 0px 0px 10px;\" align=\"center\" valign=\"top\"";
    private static Logger logger = LoggerFactory.getLogger(Command.class);

    public static boolean sendTextMail(HtmlEmail email, String title, String textBody) {
        return sendMail(email, title, textBody, MailContentType.Text);
    }

    public static boolean sendHtmlMail(HtmlEmail email, String title, String textBody) {
        return sendMail(email, title, textBody, MailContentType.Html);
    }

    public static boolean sendMail(HtmlEmail email, String title, String textBody, MailContentType emailType) {
        boolean status = false;
        try {
            if (title == null) {
                email.setSubject("You received a new email, send by " + email.getFromAddress());
            } else {
                email.setSubject(title);
            }
            switch (emailType) {
                case Html:
                    email.setHtmlMsg(textBody);
                    break;
                case Text:
                    email.setTextMsg(textBody);
                    break;
                default:
                    email.setMsg(textBody);
            }
            email.send();
            status = true;
        } catch (EmailException e) {
            logger.error("could not add send email.", e);
        }
        return status;
    }

    public static boolean sendMail(MailContentType mailContentType, String host, String username, String from,
                                   String password,
                                   String title, String content, String nickname, String to, String cc, Set<String> files) {
        boolean status;
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");
        email.setHostName(host);
        if (username == null || password == null) {
            email.setAuthentication("", "");
        } else {
            email.setAuthentication(username, password);
        }
        if (from != null && (to != null || cc != null)) {
            List<String> emailTo = new ArrayList<>();
            if (to != null) {
                emailTo = CollectionCommand.convertToList(to, ";");
            }
            List<String> emailCC = new ArrayList<>();
            if (cc != null) {
                emailCC = CollectionCommand.convertToList(cc, ";");
            }
            emailTo.forEach(t -> {
                try {
                    email.addTo(t);
                } catch (EmailException e) {
                    logger.warn("email exception could not add target email address {}", t);
                }
            });
            emailCC.forEach(c -> {
                try {
                    email.addCc(c);
                } catch (EmailException e) {
                    logger.warn("could not add cc address :{}", c);
                }
            });
            try {
                if (nickname != null) {
                    email.setFrom(from, nickname);
                } else {
                    email.setFrom(from);
                }
                if (files.size() != 0) {
                    // MimeBodyPart mimeBodyPart=new MimeBodyPart();
                    // MimeMultipart mimeMultipart=new MimeMultipart();
                    // mimeBodyPart.setContent(content,"text/html; charset=UTF-8");
                    // mimeMultipart.addBodyPart(mimeBodyPart);
                    // mimeMultipart.addBodyPart(new DataHandler(file));
                    // setFileName(file.split("/").last)
                    files.forEach(f -> {
                        File file1 = new File(f);
                        if (file1.isFile()) {
                            try {
                                email.attach(file1.getAbsoluteFile());
                            } catch (EmailException e) {
                                logger.error("could not find file {} ", file1);
                            }
                        }
                    });
                }
                sendMail(email, title, content, mailContentType);
                status = true;
            } catch (EmailException e) {
                status = false;
                logger.error("send formatMessage fail from address not right {}", from);
            }

        } else {
            logger.error("did not contain from or to address for the url. send formatMessage fail.");
            status = false;
        }
        return status;
    }

    public static boolean sendMail(MailContentType mailContentType, String url, String title, String content) {
        try {
            return sendMail(mailContentType, UriCommand.uriBuilder(url), title, content);
        } catch (Exception e) {
            logger.error("url parse error for send email", e);
            return false;
        }
    }

    public static boolean sendMail(MailContentType mailContentType, String url, String from, String to, String cc,
                                   String title, String content, List<String> files) {
        Set<String> setFiles = new HashSet<>(files);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : setFiles) {
            stringBuffer.append(string + ";");
        }
        return sendMail(mailContentType, url, from, to, cc, title, content);
    }

    public static boolean sendMail(MailContentType mailContentType, String url, String from, String to, String cc,
                                   String title, String content, String file) {
        url = url + "&file=" + file;
        return sendMail(mailContentType, url, from, to, cc, title, content);
    }

    public static boolean sendMail(MailContentType mailContentType, String url, String from, String to, String cc,
                                   String title, String content) {
        try {
            UriBuilder uriBuilder = UriCommand.uriBuilder(url);
            if (from != null) {
                uriBuilder.addParameter("from", from);
            }
            if (to != null) {
                uriBuilder.addParameter("to", to);
            }
            if (cc != null) {
                uriBuilder.addParameter("cc", cc);
            }
            return sendMail(mailContentType, uriBuilder, title, content);
        } catch (Exception e) {
            logger.error("url parse error for send email", e);
            return false;
        }
    }

    public static boolean sendMail(Message message) {
        MailContentType mailContentType = MailContentType.valueOf(message.getStatus());
        boolean status = false;
        if (mailContentType == null) {
            throw new RuntimeException(
                    "error formatMessage contentType code, did not support the message status " + message.getStatus());
        } else {
            if (message.getInfo().containsKey("url")) {
                Object data;
                UriBuilder uriBuilder = UriCommand.uriBuilder(message.getInfo().get("url").toString());
                String title = null;
                if (message.getInfo().containsKey("title")) {
                    title = message.getInfo().get("title").toString();
                }
                if (message.getInfo().containsKey("from")) {
                    Set<String> sets = new HashSet<>();
                    String from = message.getInfo().get("from").toString();
                    String to = message.getInfo().get("to").toString();
                    if (from == null || to == null) {
                        throw new RuntimeException("should define from and to address in Message Entity.");
                    }
                    sets.addAll(CollectionCommand.convertToList(from, ";"));
                    for (String str : sets) {
                        uriBuilder.addParameter("from", str);
                    }
                    sets.clear();

                    sets.addAll(CollectionCommand.convertToList(to, ";"));
                    for (String str : sets) {
                        uriBuilder.addParameter("to", str);
                    }
                    sets.clear();

                    String cc = message.getInfo().get("cc").toString();
                    if (cc != null) {
                        sets.addAll(CollectionCommand.convertToList(to, ";"));
                        for (String str : sets) {
                            uriBuilder.addParameter("to", str);
                        }
                        sets.clear();
                    }

                    Object object = message.getInfo().get("file");
                    if (object != null) {
                        if (object instanceof Collection) {
                            Collection list = (Collection) object;
                            sets.addAll(list);
                        } else if (object instanceof String) {
                            sets.addAll(CollectionCommand.convertToList(object.toString()));
                        }
                        for (String str : sets) {
                            uriBuilder.addParameter("file", str);
                        }
                    }
                }
                data = message.getData();
                if (message.getStatus() != null && message.getStatus() == 1) {
                    data = formatMessage(message);
                }
                status = MailCommand.sendMail(mailContentType, uriBuilder, title, data.toString());

            } else {
                logger.error("message info did not contain email url");
            }
        }
        return status;
    }

    public static boolean sendMail(Message message, SimpleMail simpleMail) {
        return sendMail(formatMessage(message, simpleMail));
    }

    public static boolean sendMail(SimpleMail simpleMail) {
        Message message = new Message();
        message.setStatus(1);
        message.setCode(25);
        message.setData(simpleMail.getContent());
        formatMessage(message, simpleMail);
        message.setData(formatMessage(message));
        return sendMail(message);
    }

    public static Message formatMessage(Message message, SimpleMail simpleMail) {
        if (simpleMail.getUrl() != null) {
            message.getInfo().put("url", simpleMail.getUrl());
        }
        if (simpleMail.getAuthor() != null) {
            message.getInfo().put("author", simpleMail.getAuthor());
        }
        if (simpleMail.getFrom() != null) {
            message.getInfo().put("from", simpleMail.getFrom());
        }
        if (simpleMail.getCc() != null) {
            message.getInfo().put("cc", simpleMail.getCc());
        }
        if (simpleMail.getContact() != null) {
            message.getInfo().put("contact", simpleMail.getContact());
        }
        if (simpleMail.getContent() != null) {
            message.setData(simpleMail.getContent());
        }
        if (simpleMail.getFile() != null) {
            message.getInfo().put("file", simpleMail.getFile());
        }
        if (simpleMail.getNickname() != null) {
            message.getInfo().put("nickname", simpleMail.getNickname());
        }
        if (simpleMail.getMessage() != null) {
            message.setMessage(simpleMail.getMessage());
        }
        if (simpleMail.getType() != null) {
            message.setType(simpleMail.getType());
        }
        if (simpleMail.getCopyright() != null) {
            message.getInfo().put("copyright", simpleMail.getCopyright());
        }
        if (simpleMail.getMore() != null) {
            message.getInfo().put("more", simpleMail.getMore());
        }
        if (simpleMail.getHeadline() != null) {
            message.getInfo().put("headline", simpleMail.getHeadline());
        }
        if (message.getStatus() == null) {
            message.setStatus(1);
        }
        return message;
    }

    public static String formatMessage(Message message) {
        String result = null;
        logger.info("mail default root resource:{}",
                MailCommand.class.getClassLoader().getResource("mail.html").getPath());
        String data = null;
        data = FileCommand.readUtfResource("mail.html", MailCommand.class.getClassLoader());
        Document document = Jsoup.parse(data);
        Element element = document.getElementById("mail_body");
        Object title = message.getInfo().get("headline");
        Object more = message.getInfo().get("more");
        Object contact = message.getInfo().get("contact");
        String subHeaderLine = message.getType();
        String messageData = message.getMessage();
        if (title != null) {
            document.getElementById("message:title").appendChild(new TextNode(title.toString(), ""));
        }
        if (subHeaderLine != null) {
            document.getElementById("message:subTitle").prependChild(new TextNode(subHeaderLine, ""));
        }
        if (messageData != null) {
            document.getElementById("message:subTitle").appendChild(new TextNode(messageData, ""));
        }
        if (more != null || contact != null) {
            data = FileCommand.readUtfResource("element.html", MailCommand.class.getClassLoader());
            Document elementDocument = Jsoup.parse(data);
            Element moreElemnt = elementDocument.getElementById("more");
            if (more != null) {
                moreElemnt.getElementById("left").attr("href", more.toString());
            }
            if (contact != null) {
                moreElemnt.getElementById("right").attr("href", contact.toString());
            }
            document.getElementById("message:subTitle").appendChild(moreElemnt);
        }
        Object copyRight = message.getInfo().get("copyright");
        Object author = message.getInfo().get("author");
        if (copyRight != null) {
            document.getElementById("copyright").text(copyRight.toString());
        }
        if (author != null) {
            document.getElementById("author").text(author.toString());
        }
        if (message.getData() != null) {
            Object object = message.getData();
            if (object instanceof String) {
                String string = object.toString();
                result = string.replaceAll(Pattern.quote("{{row}}"), row);
                result = result.replaceAll(Pattern.quote("{{column}}"), row);
            } else if (object instanceof MapJson) {
                // skip
            } else {
                logger.warn("did not support other type.");
            }
        }
        try {
            if (result != null) {
                element.append(result);
            }
        } catch (Exception e) {

        }
        result = document.outerHtml();
        message.setDatum(result);
        return result;
    }

    public static boolean sendMail(MailContentType mailContentType, UriBuilder uriBuilder, String title,
                                   String content) {
        boolean status = false;
        if (uriBuilder.getScheme().equalsIgnoreCase("mail")) {
            String username = uriBuilder.getUserInfo();
            String host = uriBuilder.getHost();
            String from = null;
            String to = null;
            String cc = null;
            String nickname = null;
            Set<String> files = new HashSet<>();
            String password = null;
            for (NameValuePair nameValuePair : uriBuilder.getQueryParams()) {
                if (nameValuePair.getName().equalsIgnoreCase("PASSWORD")) {
                    password = nameValuePair.getValue();
                } else if (nameValuePair.getName().equalsIgnoreCase("USERNAME")) {
                    username = nameValuePair.getValue();
                } else if (nameValuePair.getName().equalsIgnoreCase("from")) {
                    from = nameValuePair.getValue();
                } else if (nameValuePair.getName().equalsIgnoreCase("to")) {
                    if (to == null) {
                        to = nameValuePair.getValue();
                    } else {
                        to = to + ";" + nameValuePair.getValue();
                    }
                } else if (nameValuePair.getName().equalsIgnoreCase("cc")) {
                    if (cc == null) {
                        cc = nameValuePair.getValue();
                    } else {
                        cc = cc + ";" + nameValuePair.getValue();
                    }
                } else if (nameValuePair.getName().equalsIgnoreCase("nickname")) {
                    nickname = nameValuePair.getValue();
                } else if (nameValuePair.getName().equalsIgnoreCase("file")) {
                    try {
                        files = new HashSet<>(CollectionCommand.convertToList(nameValuePair.getValue()));
                    } catch (Exception e) {
                        logger.error("send email from url could not know file type not know.");
                    }
                }
            }
            if (from == null || (cc == null && to == null)) {
                logger.error("did not found from address and to address.");
                return false;
            } else {
                return sendMail(mailContentType, host, username, from, password, title, content, nickname, to, cc,
                        files);
            }
        } else {
            logger.info("could not parse uri, not formatMessage url {}", uriBuilder.buildString());
        }
        return status;
    }

    public enum MailContentType {
        Text(0), Html(1), Simple(2);
        private int type;

        MailContentType(int i) {
            this.type = i;
        }

        public static MailContentType valueOf(int statusCode) {
            MailContentType status = resolve(statusCode);
            if (status == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            } else {
                return status;
            }
        }

        @Nullable
        public static MailContentType resolve(int statusCode) {
            MailContentType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                MailContentType status = var1[var3];
                if (status.type == statusCode) {
                    return status;
                }
            }
            return null;
        }

        public int getType() {
            return type;
        }
    }
}

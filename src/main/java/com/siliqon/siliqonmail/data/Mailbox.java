package com.siliqon.siliqonmail.data;

import java.util.List;

public class Mailbox {

    List<Mail> mailbox;
    public Mailbox(List<Mail> existingMail) {
        this.mailbox = existingMail;
    }

    public List<Mail> getMail() {
        return mailbox;
    }
    public void setMail(Mail newMail) {
        this.mailbox.add(newMail);
    }
    public void deleteAllMail() {
        this.mailbox.clear();
    }

    public void sendMail(Mail mail) {
        this.mailbox.add(mail);
    }
    public void deleteMail(int id) {
        this.mailbox.remove(id);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.gruppe6.model.applicationLogic.imap;

import de.bht.fpa.mail.gruppe6.model.applicationLogic.EmailStrategyIF;
import de.bht.fpa.mail.gruppe6.model.applicationLogic.imap.IMapConnectionHelper;
import de.bht.fpa.mail.gruppe6.model.applicationLogic.imap.IMapEmailConverter;
import de.bht.fpa.mail.gruppe6.model.data.Account;
import de.bht.fpa.mail.gruppe6.model.data.Email;
import de.bht.fpa.mail.gruppe6.model.data.Folder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Nessi
 */
public class ImapEmailStrategy implements EmailStrategyIF {

    private Account account;
    private Store store;

    public ImapEmailStrategy(Account account) {
        if (account != null) {
            this.account = account;
            store = IMapConnectionHelper.connect(account);
        }

    }

    @Override
    public void loadEmails(Folder f) {
        if (f == null || f.getName().equals(account.getName()) || store == null) {
            return;
        }
        try {
            if (!f.getLoaded() && f.getEmails().isEmpty() && store.getFolder(f.getPath()) != null) {
                javax.mail.Folder topFolder = store.getFolder(f.getPath());
                if (topFolder != null) {
                    topFolder.open(javax.mail.Folder.READ_ONLY);
                    Message[] emails = topFolder.getMessages();
                    for (Message x : emails) {
                        Email mail = IMapEmailConverter.convertMessage(x);
                        if (!mail.toString().contains("false")) {
                            f.addEmail(mail);
                        }
                    }
                }
                f.setLoaded();
            }

        } catch (MessagingException ex) {
            Logger.getLogger(ImapEmailStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

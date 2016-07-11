package de.bht.fpa.mail.gruppe6.model.applicationLogic;

import de.bht.fpa.mail.gruppe6.controller.AppController;
import de.bht.fpa.mail.gruppe6.model.applicationLogic.xml.XmlEmailStrategy;
import de.bht.fpa.mail.gruppe6.model.data.Email;
import de.bht.fpa.mail.gruppe6.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Nessi
 */
public class EmailManager {

    private Folder currentFolder;
    public EmailStrategyIF strategy;

    public EmailManager() {
        strategy = new XmlEmailStrategy();
    }

    public void loadEmails(Folder f) {
        if (f != null) {
            strategy.loadEmails(f);
            currentFolder = f;
        }
    }

    public void saveEmails(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(Email.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ObservableList<Email> tableinfo = AppController.tableinfo;
            if (tableinfo != null && tableinfo.size() > 0 && file != null) {
                int times = 1;
                for (Email x : tableinfo) {
                    File newpath = new File(file, "email" + times + ".xml");
                    m.marshal(x, newpath);
                    times = times + 1;
                }
            }
        } catch (JAXBException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Email> search(String pattern) {
        ArrayList<Email> ersatz = new ArrayList<>();
        for (Email x : currentFolder.getEmails()) {
            if (x.toString().toLowerCase().contains(pattern) || x.getText().toLowerCase().contains(pattern)) {
                ersatz.add(x);
            }
        }
        return ersatz;
    }

    public void setEmailStrategy(EmailStrategyIF strategy) {
        this.strategy = strategy;
    }
}

package consoleadressbook;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * 
 * @author moridin777
 */
public class AddressBookWork {
    
    private static final Logger logger=Logger.getLogger(AddressBookWork.class);
    
    /**
     * Производится чтение строк адресной книги, с выводом в консоль
     * @param pathArddressBook путь к файлу адресной книги
     */
    public void readAddressBook(String pathArddressBook) {
        File fileAdressBook =new File(pathArddressBook);
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(new FileReader(fileAdressBook));
            String tmp;
            while((tmp = bufferedReader.readLine()) != null) {
                System.out.println(tmp);
            }
            bufferedReader.close();
        }catch(IOException ex){
            logger.error(ex);
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException ex) {
                logger.error(ex);
            }
        }
    }
    
    /**
     * Поиск контакта в адресной книге, по контексту ФИО
     * @param pathArddressBook путь к файлу адресной книги
     * @param FIO Фамилия Имя Отчество контакта
     * @return <xmp>List<String></xmp> содержащий список контактов, удовлетворяющих заданному ФИО
     */
    public List<String> getContact(String pathArddressBook, String FIO) {
        List<String> contact = new ArrayList<String>();
        File fileAdressBook = new File(pathArddressBook);
        BufferedReader bufferedReader=null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileAdressBook));
            String tmp;
            while((tmp = bufferedReader.readLine()) != null){
                if(tmp.length()>0) {
                    if(tmp.split(";")[1].toLowerCase().contains(FIO.toLowerCase())) {
                        contact.add(tmp);
                    }
                }
            }
        } catch(IOException ex) {
            logger.error(ex);
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException ex) {
                logger.error(ex);
            }
        }
        return contact;
    }
    
    /**
     * Производит запись нового контакта в адресную книгу
     * @param pathArddressBook путь к файлу адресной книги
     * @param contact строка содержащая id контакта, ФИО, номер телефона и E-mail. В качестве разделителя используется символ ;
     */
    public void writeAddressBook(String pathArddressBook, String contact) {
        FileWriter fileWriteAddressBook=null;
        try {
            fileWriteAddressBook = new FileWriter(pathArddressBook, true);
            fileWriteAddressBook.write(System.getProperty("line.separator"));
            fileWriteAddressBook.write(contact);
        } catch(IOException ex) {
            logger.error(ex);
        } finally {
            try {
                fileWriteAddressBook.close();
            } catch(IOException ex) {
                logger.error(ex);
            }
        }
    }
    
    /**
     * Сообщает номер следующего id контакта
     * @param pathArddressBook путь к файлу адресной книги
     * @return id следущего по порядку контакта, в формате <h3>int</h3>
     */
    public int getNextContactID(String pathArddressBook) {
        int contactId=1;
        File fileReadAddressBook = new File(pathArddressBook);
        BufferedReader bufferedReader=null;
        
        try {
            bufferedReader = new BufferedReader(new FileReader(fileReadAddressBook));
            String tmp;
            while((tmp = bufferedReader.readLine()) != null) {
                if(tmp.length() != 0) {
                    contactId = Integer.parseInt(tmp.split(";")[0]) + 1;
                }
            }
        } catch(IOException ex) {
            logger.error(ex);
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException ex) {
                logger.error(ex);
            }
        }
        return contactId;
    }
    
    /**
     * Производит удаление контакта из адресной книги, по id контакта
     * @param pathArddressBook путь к файлу адресной книги
     * @param id контакта в адресной книге
     * @return Boolean информация об успешности удаления контакта
     */
    public Boolean removeContact(String pathArddressBook, String id) {
        Boolean remove=false;
        List<String> contacts = new ArrayList<String>();
        File fileReadAddressBook = new File(pathArddressBook);
        File addressBook = new File(pathArddressBook);
        File addressBookTmp = new File(pathArddressBook+".tmp");
        BufferedReader bufferedReader=null;
        FileWriter fileWrite=null;
        
        try {
            bufferedReader = new BufferedReader(new FileReader(fileReadAddressBook));
            String tmp;
            while((tmp = bufferedReader.readLine()) != null) {
                if(tmp.length() > 0){
                    contacts.add(tmp);
                }
            }
        } catch(IOException ex) {
            logger.error(ex);
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException ex) {
                logger.error(ex);
            }
        }
        
        try {
            if(addressBookTmp.exists()) {
                addressBookTmp.delete();
            }
            addressBook.renameTo(addressBookTmp);
            addressBook.createNewFile();
            fileWrite = new FileWriter(pathArddressBook, true);
            int newID=1;
            for(String contact:contacts) {
                if(!contact.split(";")[0].equals(id)){
                    fileWrite.write(System.getProperty("line.separator"));
                    fileWrite.write(String.valueOf(newID) + ";" + contact.split(";")[1] 
                            + ";" + contact.split(";")[2] + ";" + contact.split(";")[3]);
                    newID++;
                } else {
                    remove = true;
                }
            }
            addressBookTmp.delete();
        } catch(IOException ex) {
            logger.error(ex);
        } finally {
            try {
                fileWrite.close();
            } catch(IOException ex) {
                logger.error(ex);
            }
        }
        return remove;
    }
}

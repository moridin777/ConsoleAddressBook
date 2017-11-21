package consoleadressbook;

import java.io.*;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class ConsoleAdressBook {
    
    private final static String pathLogConf = System.getProperty("user.dir") + "/log4j.cfg";
    private final String pathAdressBook = System.getProperty("user.dir") + "/adressbook.cvs";
    private static final Logger logger = Logger.getLogger(ConsoleAdressBook.class);
    
    static{
	init();
    }
    
    public static void main(String[] args) {
        ConsoleAdressBook consoleAdressBook=new ConsoleAdressBook();
        if(consoleAdressBook.initAdderssBook()){
            consoleAdressBook.startAddressBook();
        }else{
            System.out.println("Невозможно создать адресную книгу.");
        }
    }
    
    /**
     * Инициализация приложения, создание файла адресной книги
     * @return Boolean в зависимости, от успешности создания файла
     */
    public Boolean initAdderssBook(){
        File fileAdressBook = new File(pathAdressBook);
        try{
            if(!fileAdressBook.exists()){
                fileAdressBook.createNewFile();
                logger.debug("create AB file. " + fileAdressBook.getAbsolutePath());
            }
        }catch(IOException ex){
            logger.error(ex);
        }
        return fileAdressBook.exists();
    }
    
    /**
     * Запуск сервиса для работы с адресной книгой
     */
    public void startAddressBook(){
        BufferedReader bufferedReader=null;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                System.out.println("1 - добавить контакт");
                System.out.println("2 - показать все контакты");
                System.out.println("3 - поиск по ФИО");
                System.out.println("4 - удаление контакта");
                System.out.println("q - выход");
                System.out.print("Запрос: ");
                String inputText = bufferedReader.readLine();
                if(inputText.equals("q")) {
                    break;
                }
                if(inputText.equals("1")) {
                    System.out.print("Введите ФИО: ");
                    String FIO = bufferedReader.readLine();
                    System.out.print("Введите номер: ");
                    String phone = bufferedReader.readLine();
                    System.out.print("Введите E-mail: ");
                    String email = bufferedReader.readLine();
                    if(FIO.contains(";")||phone.contains(";")||email.contains(";")) {
                        System.out.println("Вы использовали символ \";\" при вводе ФИО, телефона или E-mail, это недопустимо.");
                    } else if(FIO.length()<1) {
                        System.out.println("ФИО меньше допустимой длины.");
                    } else {
                        AddressBookWork addressBookWork = new AddressBookWork();
                        String writeText = addressBookWork.getNextContactID(pathAdressBook) + ";" + FIO + ";" + phone + ";" + email;
                        addressBookWork.writeAddressBook(pathAdressBook, writeText);
                    }
                }
                if(inputText.equals("2")) {
                    AddressBookWork addressBookWork = new AddressBookWork();
                    System.out.println("ID; ФИО; Телефон; E-mail");
                    addressBookWork.readAddressBook(pathAdressBook);
                }
                if(inputText.equals("3")) {
                    AddressBookWork addressBookWork = new AddressBookWork();
                    System.out.print("Введите ФИО: ");
                    String FIO = bufferedReader.readLine();
                    if(!FIO.isEmpty()) {
                        if(!addressBookWork.getContact(pathAdressBook, FIO).isEmpty()) {
                            System.out.println("ID; ФИО; Телефон; E-mail");
                            for(String contact : addressBookWork.getContact(pathAdressBook, FIO)) {
                                System.out.println(contact);
                            }
                        } else {
                            System.out.println("Контакт не найден.");
                        }
                        
                    }
                }
                if(inputText.equals("4")) {
                    System.out.print("Введите id контакта для удаления: ");
                    String id = bufferedReader.readLine();
                    String check = "1234567890";
                    Boolean checkOk = true;
                    for(String data : id.split("")) {
                        if(!check.contains(data)) {
                            System.out.println("Вы ввели не коректный id.");
                            checkOk = false;
                            break;
                        }
                    }
                    if(checkOk) {
                        AddressBookWork addressBookWork = new AddressBookWork();
                        if(addressBookWork.removeContact(pathAdressBook, id)) {
                            System.out.println("Контакт удален.");
                        } else {
                            System.out.println("Контакт по данному id не найден.");
                        }
                    }
                }
            }
            
        } catch(IOException ex) {
            System.out.println(ex.toString());
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException ex) {
                logger.error(ex);
            }
        }
    }
    
    private static void init(){
        try{
            DOMConfigurator.configure(pathLogConf);
        }catch(Exception ex){
            System.err.println("log4j error init: "+ex.toString());
        }		
    }
    
}

import Service.Service;

public class Main {
    public static void main(String[] args) {
       if (args.length != 3) {
           System.out.println("Ошибка, неверное количество аргументов.");
           System.out.println("1. Команда(search,stat); " +
                              "2. Имя файла для чтения(input.json);" +
                             " 3. Имя файле с результатами(output.json).");
           return;
       }

        try(Service service = new Service()) {
            service.getCommand(args[0], args[1], args[2]);
        }

    }
}
package blitzkrieg;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class BlitzKrieg {

	private static Lang currentLang = Lang.EN;
	
	public static void main(String[] args) throws Exception {
		if (System.console() == null) {
			new ProcessBuilder("cmd", "/c", "start", "cmd", "/k", "chcp 1251 && java -jar " + getJarName() + " && pause").start();
			return;
        }
		
		clear();
		try (Scanner sc = new Scanner(System.in)) {
			System.out.print("[1] English / [2] Русский: ");
			
			String input = sc.nextLine();
			switch (input) {
			case "1":
				currentLang = Lang.RU;
				break;
			case "2":
				currentLang = Lang.RU;
				break;
			default:
				System.out.println("INVALID");
				System.exit(-1);
			}
			
			System.out.print(currentLang.startposes);
			String path = sc.nextLine().replaceAll("^\"|\"$", "");
			
			List<String> lines;
			
			try {
				lines = Files.readAllLines(Paths.get(path));
			} catch (IOException e) {
			    e.printStackTrace();
			    return;
			}
			
			int[] numbers = new int[lines.size() + 2];
			numbers[0] = 0;
			numbers[numbers.length - 1] = 100;
			
			for (int i = 0; i < lines.size(); i++) {
			    numbers[i + 1] = Integer.parseInt(lines.get(i));
			}
			
			System.out.print(currentLang.pathcreated);
			String fileOut = sc.nextLine();

			try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(fileOut.replaceAll("^\"|\"$", ""))))) {
			    int stage = 1;
			    int i = 0;
			    int j = numbers.length - 1;
			    int k = j - 1;

			    while (k != i) {
			        writer.printf("Stage %d%n", stage++);
			        int tempJ = j;
			        int tempK = k;
			        while (tempK >= i) {
			            writer.printf("%d - %d%n", numbers[tempK], numbers[tempJ]);
			            tempK--;
			            tempJ--;
			        }
			        k--;
			    }

			    writer.printf("Stage %d%n", stage);
			    writer.print("0 - 100");
			    System.out.println(currentLang.filecreated);
			} catch (IOException e) {
				System.out.println(currentLang.pizdec + e.getMessage());
			}
		}
	}
	
	private static String getJarName() {
        return new java.io.File(BlitzKrieg.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
    }

	public final static void clear() {
		try {
			final String os = System.getProperty("os.name");
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
		}
	}
	
	enum Lang {
	    EN("Enter the path to the file with the startposes: ", "Enter the path/name of the file to be created: ", "FILE CREATED", "Umm: "),
	    RU("Введите путь к файлу с данными стартпозов: ", "Введите путь/название файла который будет создан: ", "ФАЙЛ СОЗДАН", "Пиздец: ");

	    final String startposes, pathcreated, filecreated, pizdec;

	    Lang(String startposes, String pathcreated, String filecreated, String pizdec) {
	        this.startposes = startposes;
	        this.pathcreated = pathcreated;
	        this.filecreated = filecreated;
			this.pizdec = pizdec;
	    }
	}
}

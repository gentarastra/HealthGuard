import java.io.*;
import java.util.*;

public class HealthGuard {
    static Map<String, UserData> penggunaData = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();
        while (true) {
            tampilkanHeader();
            String namaPengguna = mintaNamaPengguna();
            boolean lanjut = true;
            while (lanjut) {
                int pilihan = tampilkanMenu();

                switch (pilihan) {
                    case 1:
                        inputDetakJantung(namaPengguna);
                        break;
                    case 2:
                        inputTensiDarah(namaPengguna);
                        break;
                    case 3:
                        inputBMI(namaPengguna);
                        break;
                    case 4:
                        tampilkanData(namaPengguna);
                        break;
                    case 5:
                        lanjut = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Coba lagi.");
                }
            }

            System.out.println("\nSesi telah berakhir. Terima kasih, " + namaPengguna + "!\n");
            saveData();
        }
    }

    private static void tampilkanHeader() {
        System.out.println("=========================================");
        System.out.println("     SELAMAT DATANG DI APLIKASI KESEHATAN");
        System.out.println("=========================================");
    }

    private static String mintaNamaPengguna() {
        while (true) {
            System.out.print("Masukkan nama Anda untuk memulai sesi (hanya huruf dan spasi): ");
            String nama = scanner.nextLine().trim();
    
            if (!nama.matches("[a-zA-Z\\s]+")) {
                System.out.println("Nama hanya boleh berisi huruf dan spasi. Silakan coba lagi.");
                continue;
            }

            System.out.println();

            if (penggunaData.containsKey(nama)) {
                System.out.println("Halo, " + nama + "! Selamat Datang kembali. Data Anda telah terdaftar di Sistem. Silahkan melihat riwayat pada menu 4.");
            } else {
                System.out.println("Halo, " + nama + "! Selamat Datang. Data Anda kini baru terdaftar di Sistem. Silahkan pilih menu yang tersedia & Data anda akan disimpan.");
            }
    
            return nama;
        }
    }
    

    private static int tampilkanMenu() {
        System.out.println("\n=== MENU KESEHATAN ===");
        System.out.println("1. Input Data Detak Jantung");
        System.out.println("2. Input Data Tensi Darah");
        System.out.println("3. Input Data BMI");
        System.out.println("4. Tampilkan Data");
        System.out.println("5. Akhiri Sesi");
        System.out.print("Pilih menu: ");
        
        while (!scanner.hasNextInt()) {
            System.out.println("Masukkan angka antara 1-7.");
            System.out.print("Pilih menu: ");
            scanner.next();
        }
        int pilihan = scanner.nextInt();
        scanner.nextLine();
        return pilihan;
    }
    

    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data_kesehatan.txt"))) {
            penggunaData = (Map<String, UserData>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File data tidak ditemukan. Membuat file baru...");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Terjadi kesalahan saat memuat data.");
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data_kesehatan.txt"))) {
            oos.writeObject(penggunaData);
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menyimpan data.");
        }
    }

    private static void inputDetakJantung(String nama) {
        UserData data = penggunaData.getOrDefault(nama, new UserData());
        System.out.print("Masukkan detak jantung per menit: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Masukkan angka yang valid untuk detak jantung.");
            scanner.next();
        }
        int detakJantung = scanner.nextInt();
        if (detakJantung < 30 || detakJantung > 220) {
            System.out.println("Detak jantung tidak normal. Silakan masukkan nilai yang valid.");
        } else if (detakJantung < 60) {
            System.out.println("Detak jantung rendah. Konsultasikan ke dokter.");
        } else if (detakJantung <= 100) {
            System.out.println("Detak jantung normal.");
        } else {
            System.out.println("Detak jantung tinggi. Konsultasikan ke dokter.");
        }
        data.setDetakJantung(detakJantung);
        penggunaData.put(nama, data);
    }

    private static void inputTensiDarah(String nama) {
        UserData data = penggunaData.getOrDefault(nama, new UserData());
        System.out.print("Masukkan tekanan sistolik (mmHg): ");
        while (!scanner.hasNextInt()) {
            System.out.println("Masukkan angka yang valid untuk tekanan sistolik.");
            scanner.next();
        }
        int sistolik = scanner.nextInt();
        System.out.print("Masukkan tekanan diastolik (mmHg): ");
        while (!scanner.hasNextInt()) {
            System.out.println("Masukkan angka yang valid untuk tekanan diastolik.");
            scanner.next();
        }
        int diastolik = scanner.nextInt();

        if (sistolik < 90 || diastolik < 60) {
            System.out.println("Tekanan darah rendah.");
        } else if (sistolik <= 120 && diastolik <= 80) {
            System.out.println("Tekanan darah normal.");
        } else {
            System.out.println("Tekanan darah tinggi. Jaga pola hidup sehat.");
        }
        data.setTensiDarah(sistolik, diastolik);
        penggunaData.put(nama, data);
    }

    private static void inputBMI(String nama) {
        UserData data = penggunaData.getOrDefault(nama, new UserData());
        System.out.print("Masukkan berat badan (kg): ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Masukkan angka yang valid untuk berat badan.");
            scanner.next();
        }
        double berat = scanner.nextDouble();
        System.out.print("Masukkan tinggi badan (cm): ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Masukkan angka yang valid untuk tinggi badan.");
            scanner.next();
        }
        double tinggiCm = scanner.nextDouble();
    
        double tinggiM = tinggiCm / 100;
        double bmi = berat / (tinggiM * tinggiM);
    
        System.out.printf("BMI Anda: %.2f%n", bmi);
    
        if (bmi < 18.5) {
            System.out.println("Kekurangan berat badan.");
        } else if (bmi < 24.9) {
            System.out.println("Berat badan normal.");
        } else if (bmi < 29.9) {
            System.out.println("Berat badan berlebih.");
        } else {
            System.out.println("Obesitas. Konsultasikan ke ahli gizi.");
        }
    
        data.setBmi(bmi);
        data.setBeratBadan(berat);
        data.setTinggiBadan(tinggiCm);
        penggunaData.put(nama, data);
    }    

    private static void tampilkanData(String nama) {
        UserData data = penggunaData.get(nama);
        if (data == null) {
            System.out.println("Belum ada data untuk pengguna ini.");
            return;
        }
    
        System.out.println("\n=== DATA KESEHATAN ===");
        System.out.println("Nama: " + nama);
    
        int detakJantung = data.getDetakJantung();
        String detakJantungKeterangan;
        if (detakJantung == 0) {
            detakJantungKeterangan = "(-)";
        } else if (detakJantung < 60) {
            detakJantungKeterangan = "(rendah)";
        } else if (detakJantung <= 100) {
            detakJantungKeterangan = "(normal)";
        } else {
            detakJantungKeterangan = "(tinggi)";
        }
        System.out.println("Detak Jantung: " + detakJantung + " bpm " + detakJantungKeterangan);
    
        int[] tensiDarah = data.getTensiDarah();
        String tensiKeterangan;
        if (tensiDarah[0] == 0 && tensiDarah[1] == 0) {
            tensiKeterangan = "(-)";
        } else if (tensiDarah[0] < 90 || tensiDarah[1] < 60) {
            tensiKeterangan = "(rendah)";
        } else if (tensiDarah[0] <= 120 && tensiDarah[1] <= 80) {
            tensiKeterangan = "(normal)";
        } else {
            tensiKeterangan = "(tinggi)";
        }
        System.out.println("Tekanan Darah: " + tensiDarah[0] + "/" + tensiDarah[1] + " mmHg " + tensiKeterangan);
    
        double bmi = data.getBmi();
        double berat = data.getBeratBadan();
        double tinggiCm = data.getTinggiBadan();
        String bmiKeterangan;
    
        if (bmi == 0.0 || berat == 0.0 || tinggiCm == 0.0) {
            bmiKeterangan = "(-)";
            System.out.println("BMI: " + bmiKeterangan);
        } else {
            if (bmi < 18.5) {
                bmiKeterangan = "(kekurangan berat badan)";
            } else if (bmi < 24.9) {
                bmiKeterangan = "(normal)";
            } else if (bmi < 29.9) {
                bmiKeterangan = "(berat badan berlebih)";
            } else {
                bmiKeterangan = "(obesitas)";
            }
            System.out.printf("BMI: %.2f %s (Berat: %.2f kg, Tinggi: %.2f cm)%n", bmi, bmiKeterangan, berat, tinggiCm);
        }
    
        System.out.println("---------------------------");
        

        System.out.println("\nPilih tindakan selanjutnya:");
        System.out.println("1. Perbarui Data");
        System.out.println("2. Hapus Data");
        System.out.println("3. Kembali ke Menu Utama");
        System.out.print("Pilih: ");
        int pilihan = scanner.nextInt();

        switch (pilihan) {
            case 1:
                perbaruiData(nama);
                break;
            case 2:
                hapusData(nama);
                break;
            case 3:
                return;
            default:
                System.out.println("Pilihan tidak valid.");
        }
    }

    private static void perbaruiData(String nama) {
        UserData data = penggunaData.get(nama);
        if (data == null) {
            System.out.println("Data tidak ditemukan. Silakan input data terlebih dahulu.");
            return;
        }

        System.out.println("Pilih data yang ingin diperbarui:");
        System.out.println("1. Detak Jantung");
        System.out.println("2. Tensi Darah");
        System.out.println("3. BMI");
        System.out.print("Pilih: ");
        int pilihan = scanner.nextInt();

        switch (pilihan) {
            case 1:
                inputDetakJantung(nama);
                break;
            case 2:
                inputTensiDarah(nama);
                break;
            case 3:
                inputBMI(nama);
                break;
            default:
                System.out.println("Pilihan tidak valid.");
        }
    }

    private static void hapusData(String nama) {
        UserData data = penggunaData.get(nama);
        if (data == null) {
            System.out.println("Data tidak ditemukan.");
            return;
        }

        System.out.print("Apakah Anda yakin ingin menghapus data Anda? (Y/N): ");
        char konfirmasi = scanner.next().charAt(0);

        if (konfirmasi == 'Y' || konfirmasi == 'y') {
            penggunaData.remove(nama);
            System.out.println("Data Anda telah dihapus.");
        } else {
            System.out.println("Data tidak dihapus.");
        }
    }
}

class UserData implements Serializable {
    private int detakJantung;
    private int[] tensiDarah = new int[2];
    private double bmi;
    private double beratBadan; 
    private double tinggiBadan; 

    public int getDetakJantung() {
        return detakJantung;
    }

    public void setDetakJantung(int detakJantung) {
        this.detakJantung = detakJantung;
    }

    public int[] getTensiDarah() {
        return tensiDarah;
    }

    public void setTensiDarah(int sistolik, int diastolik) {
        this.tensiDarah[0] = sistolik;
        this.tensiDarah[1] = diastolik;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
    public double getBeratBadan() {
        return beratBadan;
    }
    
    public void setBeratBadan(double beratBadan) {
        this.beratBadan = beratBadan;
    }
    
    public double getTinggiBadan() {
        return tinggiBadan;
    }
    
    public void setTinggiBadan(double tinggiBadan) {
        this.tinggiBadan = tinggiBadan;
    }
}

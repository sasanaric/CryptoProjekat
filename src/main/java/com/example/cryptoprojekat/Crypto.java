package com.example.cryptoprojekat;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.*;
import java.security.cert.CRL;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class Crypto {
    public static final String PRIPREMA_PATH = "Priprema/";
    public static final String CERTS_PATH = "Priprema/certs/";
    public static final String CRL_PATH = "Priprema/crl/lista.pem";
    public static final String INDEX_PATH = "Priprema/index.txt";
    public static final String REVOKE_CERTIFICATE_BATCH = "scripts\\revokeCertificate.bat";
    public static final String GEN_CRL_BATCH = "scripts\\genCRL.bat";
    public static final String DEC_KALG_BATCH = "scripts\\decryptKeyAlg.bat";
    public static final String ENC_FILE_BATCH = "scripts\\encryptFile.bat";
    public static final String DEC_FILE_BATCH = "scripts\\decryptFile.bat";
    public static final String SIGN_FILE_BATCH = "scripts\\signFile.bat";
    public static final String VERIFY_FILE_BATCH = "scripts\\verifyFile.bat";
    public static final String GEN_KEY_BATCH = "scripts\\generatePrivateKey.bat";
    public static final String GEN_CRT_REQ_BATCH = "scripts\\generateCertificateRequest.bat";
    public static final String SIGN_CRT_REQ_BATCH = "scripts\\signCertificateRequest.bat";
    public static final String PASSWORDS_PATH = "Priprema/passwords.txt";
    public static final String SECURE_FOLDER_PATH = "Priprema/SecureFolder/";
    public static final String MAP_PATH = "Priprema/SecureFolder/map.txt";
    public static final String MAPTXT = "map.txt";
    public static final String ALG_KEY_PATH = "Priprema/alg-key.txt";
    public static final String ALG = getAlgKeySign().get(0);
    public static final String KEY = getAlgKeySign().get(1);
    public static final String SIGN = getAlgKeySign().get(2);
    public static final String ICON_CRYPTO = "crypto.png";
    public static final String ICON_ERROR = "error.png";

    public static void putUserInMap(String username) throws Exception {
        decryptFile(MAPTXT);
        BufferedWriter bw = new BufferedWriter(new FileWriter(MAP_PATH,true));
        bw.write(username+":\n");
        bw.close();
        encryptFile(MAPTXT);
        deleteFile(MAP_PATH);
    }
    public static void putUsersPasswords(String username, String password) throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PASSWORDS_PATH, true));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoded = passwordEncoder.encode(password);
        String linija = username + ":" + encoded + "\n";
        System.out.println(linija);
        bufferedWriter.append(linija);
        bufferedWriter.close();
        putUserInMap(username);
    }
    public static boolean usernameIsTaken(String username) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PASSWORDS_PATH));
        String linija;
        while ((linija = bufferedReader.readLine()) != null) {
            String[] niz = linija.split(":");
            String user = niz[0];
            if (user.equals(username)) {
                bufferedReader.close();
                return true;
            }
        }
        bufferedReader.close();
        return false;
    }
    public static boolean CheckUserPassword(String username, String password) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PASSWORDS_PATH));
        String linija;
        while ((linija = bufferedReader.readLine()) != null) {
            String[] niz = linija.split(":");
            String user = niz[0];
            String pass = niz[1];
            if (user.equals(username)) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                if (passwordEncoder.matches(password,pass)) {
                    bufferedReader.close();
                    return true;
                }
            }
        }
        bufferedReader.close();
        return false;
    }
    public static void generatePrivateKey(String username) throws Exception{
        String[] command = {"cmd", "/c", GEN_KEY_BATCH,username};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
    }
//    public static void signCertificateRequest(String name) throws Exception{
//        ProcessBuilder pb = new ProcessBuilder("cmd.exe","/c","openssl ca -in requests/"+name+".csr " +
//                "-out certs/"+name+".crt -config openssl.cnf");
//        pb.directory(new File(PRIPREMA_PATH));
//        Process process = pb.start();
//        OutputStream outputStream = process.getOutputStream();
//        outputStream.write("y\n".getBytes());
//        outputStream.write("y\n".getBytes());
//        outputStream.flush();
//        outputStream.close();
//        process.waitFor();
//    }
//    public static void generateCertificateRequest(String username,String password) throws Exception{
//        generatePrivateKey(username);
//        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "openssl req -new -key private/"+username+".key " +
//                "-out requests/"+username+".csr -config openssl.cnf -days 180");
//        pb.directory(new File(PRIPREMA_PATH));
//        Process process = pb.start();
//        OutputStream outputStream = process.getOutputStream();
//        outputStream.write("\n".getBytes());outputStream.write("\n".getBytes());outputStream.write("\n".getBytes());
//        outputStream.write("\n".getBytes());outputStream.write("\n".getBytes());
//        outputStream.write((username+"\n").getBytes());
//        outputStream.write((username+"@mail.com\n").getBytes());
//        putUsersPasswords(username,password);
//        outputStream.flush();
//        outputStream.close();
//        process.waitFor();
//
//    }
    public static void generateCertificateRequest(String username) throws Exception{
        generatePrivateKey(username);
        String[] command = {"cmd", "/c", GEN_CRT_REQ_BATCH,username};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        Process process = pb.start();
        OutputStream outputStream = process.getOutputStream();
        outputStream.write("\n".getBytes());outputStream.write("\n".getBytes());outputStream.write("\n".getBytes());
        outputStream.write("\n".getBytes());outputStream.write("\n".getBytes());
        outputStream.write((username+"\n").getBytes());
        outputStream.write((username+"@mail.com\n").getBytes());
        outputStream.flush();
        outputStream.close();
        process.waitFor();

    }
    public static void signCertificateRequest(String username) throws Exception{
        String[] command = {"cmd", "/c", SIGN_CRT_REQ_BATCH,username};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        Process process = pb.start();
        OutputStream outputStream = process.getOutputStream();
        outputStream.write("y\n".getBytes());
        outputStream.write("y\n".getBytes());
        outputStream.flush();
        outputStream.close();
        process.waitFor();
    }
//    public static void generatePrivateKey(String username) throws Exception{
//        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "openssl genrsa -out private/"+username+".key 2048");
//        pb.directory(new File(PRIPREMA_PATH));
//        Process process = pb.start();
//        process.waitFor();
//    }

    public static void registerUser(String username,String password) throws Exception{
        putUsersPasswords(username,password);
        generatePrivateKey(username);
        generateCertificateRequest(username);
        signCertificateRequest(username);
    }
    public static void revokeCertificate(String username) throws Exception{
        System.out.println("START REVOKE");
        String[] command = {"cmd", "/c", REVOKE_CERTIFICATE_BATCH,username};
        ProcessBuilder pb = new ProcessBuilder(command);
        System.out.println(pb.directory());
        pb.directory(new File(PRIPREMA_PATH));
        System.out.println(pb.directory());
        pb.redirectErrorStream(true);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("Script executed with exit code " + exitCode);
        System.out.println("revokeCertificate()");
        genCRL();
    }
    public static void genCRL() throws Exception{
        String[] command = {"cmd", "/c", GEN_CRL_BATCH};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
        System.out.println("genCRL()");
    }
    public static boolean checkCertificate(String certName) throws Exception{
        FileInputStream certificateFile = new FileInputStream(CERTS_PATH+certName);
        FileInputStream crlFile = new FileInputStream(CRL_PATH);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(certificateFile);
        CRL crl = certificateFactory.generateCRL(crlFile);
        System.out.println("SERIAL:"+cert.getSerialNumber());
        if(crl.isRevoked(cert)){
            System.out.println("Sertifikat je povucen");
            return false;
        }else {
            try{
                cert.checkValidity(new Date());
                System.out.println("Sertifikat je validan");
                return true;
            }catch (Exception e){
                System.out.println("Sertifikat je istekao");
                return false;
            }
        }
    }
    public static void removeCertificateHold(String username) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(INDEX_PATH));
        StringBuilder text= new StringBuilder();
        String linija;
        while ((linija=br.readLine())!=null){
            if((linija.contains("/CN="+username+"/"))){
                String [] niz = linija.split("\t");
                linija = "V"+"\t"+niz[1]+"\t\t"+niz[3]+"\t"+niz[4]+"\t"+niz[5];
            }
            text.append(linija).append("\n");
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(INDEX_PATH));
        bw.write(text.toString());
        bw.close();
        genCRL();
    }
    public static List<String> getFiles(String username) throws Exception{
        decryptFile(MAPTXT);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(MAP_PATH));
        String linija;
        List<String> files = new ArrayList<>();
        while ((linija = bufferedReader.readLine()) != null) {
            String[] niz = linija.split(":");
            String user = niz[0];
            if (user.equals(username)) {
                int n = niz.length;
                for (int i=1;i<n;i++){
                    String [] fajlovi = niz[i].split("=");
                    files.add(fajlovi[0]);
                    System.out.println("FILE:"+fajlovi[0]);
                    int k = fajlovi.length;
                    for (int j=1;j<k;j++){
                        System.out.print(fajlovi[j]+" ");
                    }
                    System.out.println();
                }
            }
        }
        bufferedReader.close();
        deleteFile(MAP_PATH);
        return files;
    }
    public static List<String> getFilePaths(String username,String file) throws Exception{
        decryptFile(MAPTXT);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(MAP_PATH));
        String linija;
        List<String> paths = new ArrayList<>();
        while ((linija = bufferedReader.readLine()) != null) {
            String[] niz = linija.split(":");
            String user = niz[0];
            if (user.equals(username)) {
                int n = niz.length;
                for (int i=1;i<n;i++){
                    String [] fajlovi = niz[i].split("=");
                    int k = fajlovi.length;
                    if(file.equals(fajlovi[0])) {
                        paths.addAll(Arrays.asList(fajlovi).subList(1, k));
                    }
                    System.out.println();
                }
            }
        }
        bufferedReader.close();
        deleteFile(MAP_PATH);
        return paths;
    }
    public static List<String> divideFile(String fileName) throws Exception {
        deleteFile(SECURE_FOLDER_PATH+fileName);
        int numSegments = new Random().nextInt(7)+4;
        List<String> segments = new ArrayList<>();
        File inputFile = new File(SECURE_FOLDER_PATH+fileName+".enc");
        long fileSize = inputFile.length();
        long segmentSize = fileSize / numSegments;
        long remainingSize = fileSize % numSegments;
        byte[] buffer = new byte[(int) segmentSize];
        int bytesRead;
        int segmentNumber = 1;
        int directoryNumber = 0;
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile))) {
            while (segmentNumber <= numSegments) {
                String file = generateFileName()+".txt";
                segments.add(file);
                String directoryPath = getDirectoryPath(directoryNumber);
                File outputFile = new File(directoryPath+file);
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                    long bytesToRead = segmentSize;
                    if (segmentNumber == numSegments) {
                        bytesToRead += remainingSize;
                    }
                    while (bytesToRead > 0 && (bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        bytesToRead -= bytesRead;
                    }
                    segmentNumber++;
                }
                directoryNumber++;
            }
        }
        inputFile.delete();
        return segments;
    }
    public static void assembleFile(String inputFileName, List<String> segments) throws IOException {
        File outputFile = new File(SECURE_FOLDER_PATH+inputFileName+".enc");
        byte[] buffer = new byte[1024];
        int bytesRead;
        int directoryNumber = 0;
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            for (String file : segments) {
                String directoryPath = getDirectoryPath(directoryNumber);
                File inputFile = new File(directoryPath+file);
                try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile))) {
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                directoryNumber++;
            }
        }
    }
    public static void putSegments(String username,String filename,List<String> segments) throws Exception{
        decryptFile(MAPTXT);
       BufferedReader br = new BufferedReader(new FileReader(MAP_PATH));
        StringBuilder text= new StringBuilder();
        String linija;
        while ((linija=br.readLine())!=null){
            if((linija.contains(username+":"))){
                linija+=filename;
                for (String s: segments){
                    linija+="="+s;
                }
                linija+=":";
            }
            text.append(linija).append("\n");
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(MAP_PATH));
        bw.write(text.toString());
        bw.close();
        encryptFile(MAPTXT);
        deleteFile(MAP_PATH);
    }

    public static List<String> getSegments(String username,String filename) throws Exception{
        decryptFile(MAPTXT);
        List<String> segments = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(MAP_PATH));
        StringBuilder text= new StringBuilder();
        String linija;
        while ((linija=br.readLine())!=null){
            if((linija.contains(username+":"))){
                String [] fajlova = linija.split(":");
                for (String f : fajlova){
                    if(f.startsWith(filename+"=")){
                        String [] s = f.split("=");
                        segments.addAll(Arrays.asList(s).subList(1, s.length));
                    }
                }
            }
            text.append(linija).append("\n");
        }
        br.close();
        deleteFile(MAP_PATH);
        return segments;
    }
    public static String generateFileName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    public static String getDirectoryPath(int i){
        return SECURE_FOLDER_PATH+"/D"+i+"/";
    }
    public static List<String> getAlgKeySign(){
        List<String> algKeySign = new ArrayList<>();
        try {
            String[] command = {"cmd", "/c", DEC_KALG_BATCH};
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(PRIPREMA_PATH));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();

            BufferedReader br = new BufferedReader(new FileReader(ALG_KEY_PATH));
            String linija;
            while ((linija = br.readLine()) != null) {
                algKeySign.add(linija.split(":")[1]);
            }
            br.close();
            deleteFile(ALG_KEY_PATH);
        }catch (Exception e){System.out.println("getAlgKey() Exception");}
        return algKeySign;
    }
    public static void encryptFile(String fileName) throws Exception{
        String[] command = {"cmd", "/c", ENC_FILE_BATCH,ALG,KEY,fileName};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
    }
    public static void decryptFile(String fileName) throws Exception{
        String[] command = {"cmd", "/c",DEC_FILE_BATCH,ALG,KEY,fileName};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
    }
    public static void deleteFile(String path){
        File file = new File(path);
        try{
            file.delete();
        }catch (Exception e){System.out.println("deleteFile() Exception");}
    }
    public static void signFile(String username,String filename) throws Exception{
        String[] command = {"cmd", "/c", SIGN_FILE_BATCH,SIGN,username,filename};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
    }
    public static boolean verifyFile(String username,String filename) throws Exception{
        String[] command = {"cmd", "/c", VERIFY_FILE_BATCH,SIGN,username,filename};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(PRIPREMA_PATH));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.readLine();reader.readLine();
        String linija = reader.readLine();
        System.out.println(linija);
        return "Verified OK".equals(linija);
    }

}

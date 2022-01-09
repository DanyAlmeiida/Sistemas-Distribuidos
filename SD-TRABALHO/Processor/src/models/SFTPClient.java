package models;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class SFTPClient {

    private static Session session;
    private String privateKeyPath;

    public SFTPClient() {
        try {
            connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public static void connect() throws JSchException {
        JSch jsch = new JSch();


        String server = "127.0.0.1";
        session = jsch.getSession(server);
        // Uncomment the two lines below if the FTP server requires password
        session = jsch.getSession("tester", server, 22);
        session.setPassword("password");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }


    public void upload(Path path) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.put(path.toAbsolutePath().toString(),path.getFileName().toString());
        sftpChannel.exit();
    }

    public String get_content(String filename) throws JSchException {
        StringBuilder sb = new StringBuilder();
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(sftpChannel.get(filename)));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }

        } catch (Exception e) {
            System.out.println("Exception occurred during reading file from SFTP server due to " + e.getMessage());
            e.getMessage();

        }
        sftpChannel.exit();
        return sb.toString();
    }

    public void disconnect() {
        if (session != null) {
            session.disconnect();
        }
    }
}
package com.estgv.client;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.*;

public class SFTPClient {

    private static Session session;
    private String privateKeyPath;
    public static void main(String[] args) throws JSchException {
        connect();
    }
    public static void connect() throws JSchException {
        JSch jsch = new JSch();

        // Uncomment the line below if the FTP server requires certificate
        //jsch.addIdentity("C:\\Users\\Dany-PC\\Documents\\cert1.pfx");

        String server = "localhost";
        session = jsch.getSession(server);
        // Uncomment the two lines below if the FTP server requires password
        session = jsch.getSession("sd-user", server, 21);
        session.setPassword("12345");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    public void upload(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.put(source, destination);
        sftpChannel.exit();
    }

    public void download(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.get(source, destination);
        sftpChannel.exit();
    }

    public void disconnect() {
        if (session != null) {
            session.disconnect();
        }
    }
}
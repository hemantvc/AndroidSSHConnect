package com.myapp.digitaltv.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.myapp.digitaltv.constant.FileConstant;
import com.myapp.digitaltv.utils.Debugger;

import java.io.File;

public class SshUtils {
    private static final SshUtils ourInstance = new SshUtils();
    private static final String TAG = SshUtils.class.getSimpleName();

    private final String SFTPHOST = "192.168.4.52";
    private final int SFTPPORT = 22;
    private final String SFTPUSER = "pi";
    private final String SFTPPASS = "raspberry";

    // Raspberry device
    private String removeFilePath = "/home/pi/";

    public static SshUtils getInstance() {
        return ourInstance;
    }

    private SshUtils() {
    }


    public String filePutToRaspberryPiUploading() {
        Debugger.logD(TAG, "file Put To Raspberry Pi Uploading Proce6ss Start");
        String returnMessage = "Not Complete";
        try {
            JSch ssh = new JSch();
            Session session = ssh.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
            // Man In the Middle attacks
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(SFTPPASS);

            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;

//            sftp.cd(directory);
            // If you need to display the progress of the upload, read how to do it in the end of the article

            // use the put method , if you are using android remember to remove "file://" and use only the relative path
//            sftp.put("/storage/emulated/0/digital_dev/mytest.csv", "/home/pi/mytest.csv");

            boolean success = true;


            String completePathDirect = FileConstant.ROOT_PATH + FileConstant.APP_ANDROID_TO_PI_TRAFFIC_FOLDER_NAME;
            try {
                File directory = new File(completePathDirect);
                if (directory != null) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            Debugger.logD(TAG, "pi traffic upload file By folder, file name:" + file.getName());

                            sftp.put(completePathDirect + "/" + file.getName(), removeFilePath + file.getName());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (success) {
                Debugger.logD(TAG, "The file has been uploaded successfully..");
                returnMessage = "PI_TRANSFER File copy done!";
            }

            try {
                File directory = new File(completePathDirect);
                if (directory != null) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            file.delete();
                            Debugger.logD(TAG, " delete file." + file.getName());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
            returnMessage = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            returnMessage = e.getMessage();
        } catch (Error e) {
            e.printStackTrace();
            returnMessage = e.getMessage();
        }
        Debugger.logD(TAG, returnMessage);
        return returnMessage;
    }

}

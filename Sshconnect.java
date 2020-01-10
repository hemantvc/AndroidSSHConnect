 private void filePutToRaspberryPiUploading(){

        try {
            JSch ssh = new JSch();
            Session session = ssh.getSession(SFTPUSER, SFTPHOST, 22);
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
//            sftp.put("/storage/emulated/0/digital_dev/test.csv", "/home/pi/test.csv");

            boolean success = true;


            String completePathDirect = FileConstant.ROOT_PATH + FileConstant.APP_WIFI_TRAFFIC_FOLDER_NAME;
            try {
                File directory = new File(completePathDirect);
                if (directory != null) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            Debugger.logD(TAG, "wifi traffic upload file By folder, file name:" + file.getName());

                            sftp.put(completePathDirect+"/"+file.getName(), "/home/pi/android_transfer/"+file.getName());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(success){
                // The file has been uploaded succesfully
                Debugger.logD(TAG, "The file has been uploaded succesfully..");
            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.NetworkEvent networkEvent) {

    }
    private void remoteFileDownload() {

        try {
            JSch ssh = new JSch();
            Session session = ssh.getSession(SFTPUSER, SFTPHOST, 22);
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

            // use the get method , if you are using android remember to remove "file://" and use only the relative path
            sftp.get("/home/pi/hemant.txt", "/storage/emulated/0/digital_dev/hemant.txt");

            Boolean success = true;

            if (success) {
                // The file has been succesfully downloaded
            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }


    private void listOfRemoteDirectory() {
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

            // Now that we have a channel, go to a directory first if we want .. you can give to the ls the path
            // sftp.cd("/var/www/mydirectory");

            @SuppressWarnings("unchecked")

            // Get the content of the actual path using ls instruction or use the previous string of the cd instruction
                    java.util.Vector<ChannelSftp.LsEntry> flLst = sftp.ls("/home/pi/app/");

            final int i = flLst.size();

            // show the info of every folder/file in the console
            for (int j = 0; j < i; j++) {
                ChannelSftp.LsEntry entry = flLst.get(j);
                SftpATTRS attr = entry.getAttrs();

                Debugger.logD(entry.getFilename());
                Debugger.logD(entry.getFilename()); // Remote filepath
                Debugger.logD("isDir" + attr.isDir()); // Is folder
//                Debugger.logD("isLink" + attr.isLink()); // is link
//                Debugger.logD("size" + attr.getSize()); // get size in bytes of the file
//                Debugger.logD("permissions" + attr.getPermissions()); // permissions
//                Debugger.logD("permissions_string", attr.getPermissionsString());
//                Debugger.logD("longname", entry.toString());


            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } catch (SftpException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public void show_picture_func() {
        new AsyncTask<Void, Void, Void>() {

            Drawable ddd = null;

            protected Void doInBackground(Void... unused) {
                // Background Code

                String SFTPWORKINGDIR = "/home/pi/";

                Session session = null;
                Channel channel = null;
                ChannelSftp channelSftp = null;

                try {
                    JSch jsch = new JSch();
                    session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
                    session.setPassword(SFTPPASS);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setTimeout(1000);
                    session.connect();
                    channel = session.openChannel("sftp");
                    channel.connect();
                    channelSftp = (ChannelSftp) channel;
                    channelSftp.cd(SFTPWORKINGDIR);
                    byte[] buffer = new byte[1024];
                    BufferedInputStream bis = new BufferedInputStream(channelSftp.get("hemant.txt"));

                    final InputStreamReader ir = new InputStreamReader(bis, "UTF-8");
                    final StringBuilder sb = new StringBuilder();
                    final char[] buf = new char[1024];
                    int n;
                    while ((n = ir.read(buf)) != -1) {
                        sb.append(buf, 0, n);
                    }
                    Debugger.logI(" file :" + sb.toString());

//                    ddd = Drawable.createFromStream(bis,"ddd");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                return null;
            }

            protected void onPostExecute(Void unused) {
                // Post Code
//                iv1.setImageDrawable(ddd);
                Toast.makeText(SplashScreenActivity.this, "Picture Success", Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    public void downloader() {

        try {
            JSch jsch = new JSch();

            String knownHostsFilename = System.getProperty("user.home") + "/.ssh/known_hosts";
            // jsch.setKnownHosts( knownHostsFilename );

            Session session = jsch.getSession("pi", "192.168.4.52", 22);
            {
                // "interactive" version
                // can selectively update specified known_hosts file
                // need to implement UserInfo interface
                // MyUserInfo is a swing implementation provided in
                //  examples/Sftp.java in the JSch dist
//                UserInfo ui = new MyUserInfo();
//                session.setUserInfo(ui);

                // OR non-interactive version. Relies in host key being in known-hosts file
                session.setPassword("raspberry");
            }

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.cd(".");

//            sftpChannel.get("/home/pi/hemant.txt", "/storage/emulated/0/hemant.txt" );


            InputStream stream = sftpChannel.get("/home/pi/hemant.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }

            } catch (IOException io) {
                System.out.println("Exception occurred during reading file from SFTP server due to " + io.getMessage());
                io.getMessage();

            } catch (Exception e) {
                System.out.println("Exception occurred during reading file from SFTP server due to " + e.getMessage());
                e.getMessage();

            }


            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        } catch (SftpException e) {
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        }
    }

    public static String executeRemoteCommand(String username, String password, String hostname, int port)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand("lsusb > /home/pi/hemant.txt");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

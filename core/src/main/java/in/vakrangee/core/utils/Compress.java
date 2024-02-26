package in.vakrangee.core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Compress {

    private static String TAG = "Compress";
    private Logger logger;
    int BUFFER = 1024;
    private Context context;

    private Compress(){}

    public Compress(Context context) {
        this.context = context;
        logger = Logger.getInstance(context);
    }

    //Method to zip a file or folder path provided at the specified location
    public boolean zipFileAtPath(String sourcePath, String toLocation) {

        File sourceFile = new File(sourcePath);

        if (sourceFile.exists()) {
            FileInputStream fi = null;
            FileOutputStream dest = null;
            try {
                BufferedInputStream origin = null;
                dest = new FileOutputStream(toLocation);

                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                if (sourceFile.isDirectory()) {
                    zipSubFolder(out, sourceFile, sourceFile.getParent().length());
                } else {
                    byte data[] = new byte[BUFFER];

                    //FileInputStream fi = new FileInputStream(sourcePath);
                    fi = new FileInputStream(sourceFile);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(sourceFile.getName());
                    //ZipEntry entry = new ZipEntry(sourcePath);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                out.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "zipFileAtPath : Exception : " + e.toString());
                return false;
            } finally {
                if (dest != null) {
                    try {
                        dest.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (fi != null) {
                    try {
                        fi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    //Method to add the sub folder files or directory into the zip entry
    private void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException {

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        FileInputStream fi = null;
        try {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    zipSubFolder(out, file, basePathLength);
                } else {
                    String extension = file.getPath().substring(file.getPath().lastIndexOf("."));

                    System.out.println("File Extension : " + extension);
                    byte data[] = new byte[BUFFER];

                    String unmodifiedFilePath = file.getPath();
                    String relativePath = unmodifiedFilePath.substring(basePathLength);
                    Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);

                    fi = new FileInputStream(unmodifiedFilePath);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(relativePath);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "zipSubFolder : Exception : " + e.toString());
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Method to zip a file or folder path provided at the specified location
    public boolean zipFileAtPathForAssistance(String sourcePath, String toLocation, boolean isBakFilesIncluded) {

        File sourceFile = new File(sourcePath);

        if (sourceFile.exists()) {
            FileInputStream fi = null;
            FileOutputStream dest = null;
            try {
                BufferedInputStream origin = null;
                dest = new FileOutputStream(toLocation);

                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                if (sourceFile.isDirectory()) {
                    zipSubFolderForAssistance(out, sourceFile, sourceFile.getParent().length(), isBakFilesIncluded);
                } else {
                    byte data[] = new byte[BUFFER];

                    //FileInputStream fi = new FileInputStream(sourcePath);
                    fi = new FileInputStream(sourceFile);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(sourceFile.getName());
                    //ZipEntry entry = new ZipEntry(sourcePath);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "zipFileAtPath : Exception : " + e.toString());
                return false;
            } finally {
                if (fi != null) {
                    try {
                        fi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dest != null) {
                    try {
                        dest.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    //Method to add the sub folder files or directory into the zip entry
    private void zipSubFolderForAssistance(ZipOutputStream out, File folder, int basePathLength, boolean isBakFiles) throws IOException {

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;

        for (File file : fileList) {
            try {
                FileInputStream fi = null;
                if (file.isDirectory()) {
                    zipSubFolderForAssistance(out, file, basePathLength, isBakFiles);
                } else {
                    String extension = file.getPath().substring(file.getPath().lastIndexOf("."));
                    System.out.println("File Extension : " + extension);

                    if (isBakFiles) {
                        if (file.getName().endsWith(".bak")) {
                            try {
                                byte data[] = new byte[BUFFER];

                                String unmodifiedFilePath = file.getPath();
                                String relativePath = unmodifiedFilePath.substring(basePathLength);
                                Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);

                                fi = new FileInputStream(unmodifiedFilePath);
                                origin = new BufferedInputStream(fi, BUFFER);
                                ZipEntry entry = new ZipEntry(relativePath);
                                out.putNextEntry(entry);
                                int count;
                                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                    out.write(data, 0, count);
                                }
                                origin.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (fi != null) {
                                    try {
                                        fi.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } else {
                        try {
                            if (file.getName().endsWith(".txt")) {
                                byte data[] = new byte[BUFFER];

                                String unmodifiedFilePath = file.getPath();
                                String relativePath = unmodifiedFilePath.substring(basePathLength);
                                Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);

                                fi = new FileInputStream(unmodifiedFilePath);
                                origin = new BufferedInputStream(fi, BUFFER);
                                ZipEntry entry = new ZipEntry(relativePath);
                                out.putNextEntry(entry);
                                int count;
                                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                    out.write(data, 0, count);
                                }
                                origin.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fi != null) {
                                try {
                                    fi.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.writeError(TAG, "zipSubFolderForAssistance: Error: " + e.toString());
            }
        }
    }

    /**
     * Unzip the file and folder at provided path
     *
     * @param srcFilePath
     * @param desPath
     * @return
     */
    public boolean unzipFileAtPath(String srcFilePath, String desPath) {
        InputStream is = null;
        FileOutputStream fout = null;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(srcFilePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[BUFFER];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // Get the filename
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...

				/*//Start : Commented this for Testing
                if (ze.isDirectory()) {
					File fmd = new File(desPath + filename);
					fmd.mkdirs();
					continue;
				}
				//End
				 */

                //Start : Testing ---Vasundhara 2015-11-19
                File fmd = new File(desPath + filename);
                if (ze.isDirectory()) {
                    fmd.mkdirs();
                    continue;
                } else {
                    fmd.getParentFile().mkdirs();
                }
                //End

                fout = new FileOutputStream(desPath + filename);
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.writeError(TAG, "unzipFileAtPath : IOException : " + e.toString());
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Method to unzip the downloaded edetailer zip file,
     */
    //Last Unzip code for trial
    public boolean unzipEdetailerFileAtPath(String srcFilePath, String desPath) {
        InputStream is;
        ZipInputStream zis;
        try {
            logger.writeInfo(TAG, "Unziping file Started......");

            String filename;
            is = new FileInputStream(srcFilePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[BUFFER];
            int count;

            logger.writeInfo(TAG, "While Loop STARTED .....");
            while ((ze = zis.getNextEntry()) != null) {
                // Get the filename
                filename = ze.getName();

                //Start : Testing ---Vasundhara 2015-11-19
                File fmd = new File(desPath + filename);
                logger.writeInfo(TAG, "File or Folder Name : " + fmd.getAbsolutePath());
                if (ze.isDirectory()) {
                    logger.writeInfo(TAG, "Folder : " + fmd.getName() + " | fmd.isDirectory() " + fmd.isDirectory() + " | fmd.canWrite() : " + fmd.canWrite() + " | fmd.isFile() : " + fmd.isFile() + " | fmd.exists() : " + fmd.exists());

                    if (fmd.exists()) {
                        logger.writeInfo(TAG, "Folder : fmd.exists() -> " + fmd.exists());
                        if (fmd.delete()) {
                            logger.writeInfo(TAG, "Folder : fmd.delete()-> Deleted Successfully.");
                        }
                    }
                    logger.writeInfo(TAG, "Folder : Creating Folder");
                    if (fmd.mkdir()) {
                        logger.writeInfo(TAG, fmd.getName() + " fmd.mkdir() -> Folder Created Successfully.");
                    }

                    logger.writeInfo(TAG, "Folder : Creating Sub - Folder");
                    if (fmd.mkdirs()) {
                        logger.writeInfo(TAG, fmd.getName() + " fmd.mkdirs()-> Sub-Folder Created Successfully.");
                    }
                    logger.writeInfo(TAG, "IF END - continue.");
                    continue;
                } else {
                    logger.writeInfo(TAG, "ELSE FILE : " + fmd.getName() + " | fmd.isDirectory() " + fmd.isDirectory() + " | fmd.canWrite() : " + fmd.canWrite() + " | fmd.isFile() : " + fmd.isFile() + " | +fmd.exists() : " + fmd.exists());

                    if (fmd.exists()) {
                        logger.writeInfo(TAG, "File Exists Path : " + fmd.getAbsolutePath() + " | fmd.canWrite() : " + fmd.canWrite());
                        if (fmd.delete()) {
                            logger.writeInfo(TAG, fmd.getName() + " - File deleted Successfully.");
                        }
                    }
                    logger.writeInfo(TAG, "ELSE : fmd.getParentFile() : " + fmd.getParentFile());
                    //Get Parent Folder
                    if (fmd.getParentFile().mkdirs()) {
                        logger.writeInfo(TAG, fmd.getParentFile() + " - Parent File.");
                    }
                    try {
                        FileOutputStream fout = new FileOutputStream(fmd);
                        while ((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }
                        fout.close();
                        logger.writeInfo(TAG, fmd.getName() + " - File written Successfully.");
                    } catch (Exception e) //Additional Code
                    {
                        logger.writeError(TAG, "ELSE : Error - In File Creation : " + e.toString());

                        //Create a subfolder of fmd then write a file
                        logger.writeError(TAG, "ELSE : Error - fmd.getParentFile() : " + fmd.getParentFile());

                        //Get Parent Folder
                        if (fmd.getParentFile().mkdirs()) {
                            logger.writeError(TAG, "ELSE : Error - Parent Folder Created.");
                        }

                        FileOutputStream fout = new FileOutputStream(fmd);
                        while ((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }
                        fout.close();
                        logger.writeError(TAG, fmd.getName() + "ELSE : ERROR - File written Successfully.");
                    }
                    logger.writeInfo(TAG, " ELSE END.");
                }
                //End
                zis.closeEntry();
            }
            logger.writeInfo(TAG, "While Loop END .....");
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            //logger.writeError(TAG, "File Path : "+path+" File state : "+isFileExist);
            logger.writeError(TAG, "unzipFileAtPath : IOException : " + e.toString());

            return false;
        }
        return true;
    }

    //Add more files from files list into 1 zip entry
    public boolean zipFilesListwithBackupFile(String zipFile, List<String> filesPathToZip, boolean isBakFilesInclude) {
        logger.writeInfo(TAG, "Start of zipFilesList method.");
        try {

            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            System.out.println("Output to Zip : " + zipFile);

            for (String file : filesPathToZip) {

                File sourceFile = new File(file);
                Log.i(TAG, "Fil Info: sourceFile.isFile()--> " + sourceFile.isFile() + " sourceFile.isDirectory()--> " + sourceFile.isDirectory() + " sourceFile.exists()-->" + sourceFile.exists());

                if (sourceFile.exists()) {

                    if (sourceFile.isDirectory()) {

                        zipSubFolderForAssistance(out, sourceFile, sourceFile.getParent().length(), isBakFilesInclude);

                    } else {
                        byte data[] = new byte[BUFFER];

                        //FileInputStream fi = new FileInputStream(sourcePath);
                        FileInputStream fi = new FileInputStream(sourceFile);
                        origin = new BufferedInputStream(fi, BUFFER);
                        ZipEntry entry = new ZipEntry(sourceFile.getName());
                        //ZipEntry entry = new ZipEntry(sourcePath);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                    }

                }
            }
            out.closeEntry();
            //remember close it
            out.close();
            logger.writeInfo(TAG, "End of zipFilesList method.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "zipFilesList : Exception : " + e.toString());
            return false;
        }
        return true;
    }

    /**
     * Method to unzip the downloaded reference zip file,
     */
    //Last Unzip code for trial
    public boolean unzipReferenceFileAtPath(String srcFilePath, String desPath) {
        InputStream is;
        ZipInputStream zis;
        try {
            logger.writeInfo(TAG, "Unziping file Started......");

            String filename;
            is = new FileInputStream(srcFilePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[BUFFER];
            int count;

            logger.writeInfo(TAG, "While Loop STARTED .....");
            while ((ze = zis.getNextEntry()) != null) {
                // Get the filename
                filename = ze.getName();

                //Start : Testing ---Vasundhara 2015-11-19
                File fmd = new File(desPath + filename);
                logger.writeInfo(TAG, "File or Folder Name : " + fmd.getAbsolutePath());
                if (ze.isDirectory()) {
                    logger.writeInfo(TAG, "Folder : " + fmd.getName() + " | fmd.isDirectory() " + fmd.isDirectory() + " | fmd.canWrite() : " + fmd.canWrite() + " | fmd.isFile() : " + fmd.isFile() + " | fmd.exists() : " + fmd.exists());

                    if (fmd.exists()) {
                        logger.writeInfo(TAG, "Folder : fmd.exists() -> " + fmd.exists());
                        if (fmd.delete()) {
                            logger.writeInfo(TAG, "Folder : fmd.delete()-> Deleted Successfully.");
                        }
                    }
                    logger.writeInfo(TAG, "Folder : Creating Folder");
                    if (fmd.mkdir()) {
                        logger.writeInfo(TAG, fmd.getName() + " fmd.mkdir() -> Folder Created Successfully.");
                    }

                    logger.writeInfo(TAG, "Folder : Creating Sub - Folder");
                    if (fmd.mkdirs()) {
                        logger.writeInfo(TAG, fmd.getName() + " fmd.mkdirs()-> Sub-Folder Created Successfully.");
                    }
                    logger.writeInfo(TAG, "IF END - continue.");
                    continue;
                } else {
                    logger.writeInfo(TAG, "ELSE FILE : " + fmd.getName() + " | fmd.isDirectory() " + fmd.isDirectory() + " | fmd.canWrite() : " + fmd.canWrite() + " | fmd.isFile() : " + fmd.isFile() + " | +fmd.exists() : " + fmd.exists());

                    if (fmd.exists()) {
                        logger.writeInfo(TAG, "File Exists Path : " + fmd.getAbsolutePath() + " | fmd.canWrite() : " + fmd.canWrite());
                        if (fmd.delete()) {
                            logger.writeInfo(TAG, fmd.getName() + " - File deleted Successfully.");
                        }
                    }
                    logger.writeInfo(TAG, "ELSE : fmd.getParentFile() : " + fmd.getParentFile());
                    //Get Parent Folder
                    if (fmd.getParentFile().mkdirs()) {
                        logger.writeInfo(TAG, fmd.getParentFile() + " - Parent File.");
                    }
                    try {
                        FileOutputStream fout = new FileOutputStream(fmd);
                        while ((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }
                        fout.close();
                        logger.writeInfo(TAG, fmd.getName() + " - File written Successfully.");
                    } catch (Exception e) //Additional Code
                    {
                        logger.writeError(TAG, "ELSE : Error - In File Creation : " + e.toString());

                        //Create a subfolder of fmd then write a file
                        logger.writeError(TAG, "ELSE : Error - fmd.getParentFile() : " + fmd.getParentFile());

                        //Get Parent Folder
                        if (fmd.getParentFile().mkdirs()) {
                            logger.writeError(TAG, "ELSE : Error - Parent Folder Created.");
                        }

                        FileOutputStream fout = new FileOutputStream(fmd);
                        while ((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }
                        fout.close();
                        logger.writeError(TAG, fmd.getName() + "ELSE : ERROR - File written Successfully.");
                    }
                    logger.writeInfo(TAG, " ELSE END.");
                }
                //End
                zis.closeEntry();
            }
            logger.writeInfo(TAG, "While Loop END .....");
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            //logger.writeError(TAG, "File Path : "+path+" File state : "+isFileExist);
            logger.writeError(TAG, "unzipFileAtPath : IOException : " + e.toString());

            return false;
        }
        return true;
    }

    //Method to zip list of files and folder in one zip.
    public boolean zipFilesList(String zipFile, List<String> filesPathToZip) {
        logger.writeInfo(TAG, "Start of zipFilesList method.");
        BufferedInputStream origin = null;

        try {
            FileOutputStream dest = new FileOutputStream(zipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            System.out.println("Output to Zip : " + zipFile);

            for (String file : filesPathToZip) {

                File sourceFile = new File(file);
                Log.i(TAG, "Fil Info: sourceFile.isFile()--> " + sourceFile.isFile() + " sourceFile.isDirectory()--> " + sourceFile.isDirectory() + " sourceFile.exists()-->" + sourceFile.exists());

                if (sourceFile.exists()) {

                    if (sourceFile.isDirectory()) {
                        zipSubFolder(out, sourceFile, sourceFile.getParent().length());
                    } else {
                        byte data[] = new byte[BUFFER];

                        //FileInputStream fi = new FileInputStream(sourcePath);
                        FileInputStream fi = new FileInputStream(sourceFile);
                        origin = new BufferedInputStream(fi, BUFFER);
                        ZipEntry entry = new ZipEntry(sourceFile.getName());
                        //ZipEntry entry = new ZipEntry(sourcePath);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                    }

                }
            }
            out.closeEntry();
            //remember close it
            out.close();
            logger.writeInfo(TAG, "End of zipFilesList method.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "zipFilesList : Exception : " + e.toString());
            return false;
        } finally {
         /*   if(fi != null)
            {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

        }
        return true;
    }

    //Method to zip a file or folder path provided at the specified location
    public boolean zipFileAtPathExcludingSomeFiles(String sourcePath, String toLocation, String[] filesToBeExcluded) {

        File sourceFile = new File(sourcePath);

        if (sourceFile.exists()) {
            FileInputStream fi = null;
            FileOutputStream dest = null;
            try {
                BufferedInputStream origin = null;
                dest = new FileOutputStream(toLocation);

                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                if (sourceFile.isDirectory()) {
                    zipSubFolderForAssistanceExcludingSomeFiles(out, sourceFile, sourceFile.getParent().length(), filesToBeExcluded);
                } else {
                    byte data[] = new byte[BUFFER];

                    //FileInputStream fi = new FileInputStream(sourcePath);
                    fi = new FileInputStream(sourceFile);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(sourceFile.getName());
                    //ZipEntry entry = new ZipEntry(sourcePath);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "zipFileAtPath : Exception : " + e.toString());
                return false;
            } finally {
                if (fi != null) {
                    try {
                        fi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (dest != null) {
                    try {
                        dest.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    //Method to add the sub folder files or directory into the zip entry
    @SuppressLint("LongLogTag")
    private void zipSubFolderForAssistanceExcludingSomeFiles(ZipOutputStream out, File folder, int basePathLength, String[] filesToBeExcluded) throws IOException {

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        FileInputStream fi = null;
        try {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    boolean IsFilesMatched = IsFilesMatched(filesToBeExcluded, file);
                    if (!IsFilesMatched) {
                        zipSubFolderForAssistanceExcludingSomeFiles(out, file, basePathLength, filesToBeExcluded);
                    }
                } else {
                    String extension = file.getPath().substring(file.getPath().lastIndexOf("."));
                    if (!extension.equalsIgnoreCase(".lock")) {
                        System.out.println("File Extension : " + extension);
                        byte data[] = new byte[BUFFER];

                        String unmodifiedFilePath = file.getPath();
                        String relativePath = unmodifiedFilePath.substring(basePathLength);
                        Log.i("ZIP zipSubFolderForAssistanceExcludingSomeFiles", "Relative Path : " + relativePath);

                        fi = new FileInputStream(unmodifiedFilePath);
                        origin = new BufferedInputStream(fi, BUFFER);
                        ZipEntry entry = new ZipEntry(relativePath);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.writeError(TAG, "zipSubFolderForAssistanceExcludingSomeFiles : Exception : " + e.toString());
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean IsFilesMatched(String[] filesToBeExcluded, File file) {

        for (int i = 0; i < filesToBeExcluded.length; i++) {
            boolean IsMatched = file.getName().equalsIgnoreCase(filesToBeExcluded[i]);
            if (IsMatched)
                return true;

        }
        return false;
    }

}
package com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileItemStream;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

// [START bookshelf_cloud_storage_client]
public class CloudStorageHelper {

	private final Logger logger = Logger.getLogger(CloudStorageHelper.class.getName());
	private static Storage storage = null;

	// [START bookshelf_cloud_storage_client_init]
	static {
		storage = StorageOptions.getDefaultInstance().getService();
	}
	// [END bookshelf_cloud_storage_client_init]

	// [START bookshelf_cloud_storage_client_upload_file]

	/**
	 * Uploads a file to Google Cloud Storage to the bucket specified in the
	 * BUCKET_NAME environment variable, appending a timestamp to end of the
	 * uploaded filename.
	 */
	public String uploadFile(FileItemStream fileStream, final String bucketName) throws IOException, ServletException {
		checkFileExtension(fileStream.getName());

		System.out.println("FileStream name: " + fileStream.getName() + "\nBucket name: " + bucketName);

		DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
		DateTime dt = DateTime.now(DateTimeZone.UTC);
		String dtString = dt.toString(dtf);
		final String fileName = fileStream.getName() + dtString;

		// the inputstream is closed by default, so we don't need to close it here
		@SuppressWarnings("deprecation")
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, fileName)
				// Modify access list to allow all users with link to read file
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build(),
				fileStream.openStream());
		logger.log(Level.INFO, "Uploaded file {0} as {1}", new Object[] { fileStream.getName(), fileName });
		// return the public download link
		return blobInfo.getMediaLink();
	}

	/** Checks that the file extension is supported. */
	private void checkFileExtension(String fileName) throws ServletException {
		if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
			String[] allowedExt = { ".jpg", ".jpeg", ".png", ".gif" };
			for (String ext : allowedExt) {
				if (fileName.endsWith(ext)) {
					return;
				}
            }
            logger.log(Level.INFO, "file must be an image");
		}
	}
}

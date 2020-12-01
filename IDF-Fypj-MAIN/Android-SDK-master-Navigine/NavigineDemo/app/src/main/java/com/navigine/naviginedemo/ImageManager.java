package com.navigine.naviginedemo;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;


public class ImageManager {
    /*
 **Only use Shared Key authentication for testing purposes!**
 Your account name and account key, which give full read/write access to the associated Storage account,
 will be distributed to every person that downloads your app.
 This is **not** a good practice as you risk having your key compromised by untrusted clients.
 Please consult following documents to understand and use Shared Access Signatures instead.
 https://docs.microsoft.com/en-us/rest/api/storageservices/delegating-access-with-a-shared-access-signature
 and https://docs.microsoft.com/en-us/azure/storage/common/storage-dotnet-shared-access-signature-part-1
 */
    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=idfimages;AccountKey=E2SeSXobYGEtkwkjCUUm68UK9in4Uy+xjCQhuKjPj9EtO92RJJaZd6G4cIEJrjmgGSQ9KyCO/ZKJBWiL+zYHBg==;EndpointSuffix=core.windows.net";
            //"DefaultEndpointsProtocol=https;"
            //+ "AccountName=[idfphotos];"
            //+ "AccountKey=[QJbltd89LvFz1D5A4vxTgtSA1Jh7bFt9UBz5bbS2s1fB1tJTa8lytr3QR0XgdqGXGvh3QQzAFjLdRZU7lqbV4A==];"


    // testing
    //DefaultEndpointsProtocol=https;
    //AccountName=idfimages;
    //AccountKey=QJbltd89LvFz1D5A4vxTgtSA1Jh7bFt9UBz5bbS2s1fB1tJTa8lytr3QR0XgdqGXGvh3QQzAFjLdRZU7lqbV4A==;
    //EndpointSuffix=core.windows.net
    private static CloudBlobContainer getContainer() throws Exception {
        // Retrieve storage account from connection-string.

        CloudStorageAccount storageAccount = CloudStorageAccount
                .parse(storageConnectionString);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to a container.
        // The container name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference("l309");

        return container;
    }

    public static String UploadImage(InputStream image, int imageLength) throws Exception {
        CloudBlobContainer container = getContainer();

        container.createIfNotExists();

        String imageName = randomString(10);

        CloudBlockBlob imageBlob = container.getBlockBlobReference(imageName);
        imageBlob.upload(image, imageLength);

        return imageName;

    }


    //display images inside blob container
    public static String[] ListImages() throws Exception{
        CloudBlobContainer container = getContainer();

        Iterable<ListBlobItem> blobs = container.listBlobs();


        LinkedList<String> blobNames = new LinkedList<>();
        for(ListBlobItem blob: blobs) {
            blobNames.add(((CloudBlockBlob) blob).getName());
        }

        return blobNames.toArray(new String[blobNames.size()]);
    }

    public static void GetImage(String name, OutputStream imageStream, long imageLength) throws Exception {
        CloudBlobContainer container = getContainer();

        CloudBlockBlob blob = container.getBlockBlobReference(name);

        if(blob.exists()){
            blob.downloadAttributes();

            imageLength = blob.getProperties().getLength();

            blob.download(imageStream);
        }
    }

    static final String validChars = "abcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( validChars.charAt( rnd.nextInt(validChars.length()) ) );
        return sb.toString();
    }

}

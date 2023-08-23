package com.test.GraphAPITest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.DriveItemSearchParameterSet;
import com.microsoft.graph.models.DriveItemUploadableProperties;
import com.microsoft.graph.models.ListItem;
import com.microsoft.graph.models.UploadSession;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import com.microsoft.graph.requests.DriveItemSearchCollectionPage;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.ListCollectionPage;
import com.microsoft.graph.requests.ListItemCollectionPage;
import com.microsoft.graph.serializer.AdditionalDataManager;
import com.microsoft.graph.tasks.IProgressCallback;

public class UploadToSharePoint {

    private InputStream getInputStream() throws FileNotFoundException {
        // Get an input stream for the file
        InputStream fileStream = new FileInputStream("/data/code/2019/sharepointgraphsdkpoc/src/main/resources/sample.txt");
        return fileStream;
    }

    private void getFileName(InputStream inputStream) throws FileNotFoundException {
        InputStream fileStream = new FileInputStream("/data/code/2019/sharepointgraphsdkpoc/src/main/resources/sample.txt");
    }

    private long getStreamSize(InputStream fileStream) throws IOException {
        long streamSize = (long)fileStream.available();
        return streamSize;
    }

    // Create a callback used by the upload provider
    IProgressCallback callback = new IProgressCallback() {
        @Override
        // Called after each slice of the file is uploaded
        public void progress(final long current, final long max) {
            System.out.println(
                    String.format("Uploaded %d bytes of %d total bytes", current, max)
            );
        }

        public void success(final DriveItem result) {
            System.out.println(
                    String.format("Uploaded file with ID: %s", result.id)
            );
        }

        public void failure(final ClientException ex) {
            System.out.println(
                    String.format("Error uploading file: %s", ex.getMessage())
            );
        }
    };

	public void setUploadSession() throws Exception {
		final GraphServiceClient graphClient = new AuthenticationProvider().getAuthProvider();
		List<Option> metadata = new ArrayList();
		metadata.add(new QueryOption("@microsoft.graph.conflictBehavior", "rename"));
		DriveItem item = graphClient.drives("b!Bf5ZbVGRzkCi6vykP07f-fAJhu7HyDJBnOm7SiuZiye6YdI-0M4iT6vljGfNedy8")
				.items("01KB24HFZEP4I4REKAD5FILORYMLYVIQGH").itemWithPath("sample")
				.content().buildRequest(metadata).put("Subha Chandra".getBytes());
		
		 graphClient.drives("b!Bf5ZbVGRzkCi6vykP07f-fAJhu7HyDJBnOm7SiuZiye6YdI-0M4iT6vljGfNedy8")
			.items("01KB24HFYDY32Y2VXL3RGZKEZCXJBU7BGJ").itemWithPath("sample")
			.content().buildRequest(metadata).put("Subha Chandra".getBytes());
		 
		// upload to share point
		
//dev maria : 01GZRZSZLO4CXRG6T5JRA3S7ZOZ5DL5YAO	
//uat maria : 01KB24HFZEP4I4REKAD5FILORYMLYVIQGH
//uat uat : 01KB24HFYDY32Y2VXL3RGZKEZCXJBU7BGJ

	}
	
	
	public void getFile() throws Exception {
		LinkedList<Option> requestOptions = new LinkedList<>();
		requestOptions.add(new QueryOption("expand", "items(expand=fields)"));
		final GraphServiceClient graphClient = new AuthenticationProvider().getAuthProvider();
		String vaultSearch = "maria_1234"; //imanage_1234  EndFile_231231 maria_1234
		DriveItemSearchCollectionPage search = graphClient.sites("7tp7r4.sharepoint.com,01c01712-1760-4955-b9a8-0c021bb84186,ee8609f0-c8c7-4132-9ce9-bb4a2b998b27").drive().root()
		.search(DriveItemSearchParameterSet.newBuilder().withQ(vaultSearch).build()).buildRequest().get();
		
		System.out.println(search);
		System.out.println(search.getCurrentPage());
		System.out.println(search.getCount());
		List<DriveItem> driveItem =  search.getCurrentPage();
		
		DriveItem di = driveItem.get(0);
		System.out.println(driveItem.get(0).name);
		ListItem Li = di.listItem;
		System.out.println("list Item: "+di.listItem.fields);
		System.out.println(Li.name);
		
		
		
		ListCollectionPage listCollectionPage = graphClient.sites("7tp7r4.sharepoint.com,01c01712-1760-4955-b9a8-0c021bb84186,ee8609f0-c8c7-4132-9ce9-bb4a2b998b27")
				.lists().buildRequest(requestOptions).filter("displayName eq 'Documents'").get();
		System.out.println(listCollectionPage);
		System.out.println(listCollectionPage.getCurrentPage());
		System.out.println(listCollectionPage.getNextPage());
		System.out.println(listCollectionPage.getCount());
		com.microsoft.graph.models.List list = listCollectionPage.getCurrentPage().get(0);
		String listId = list.id;
		System.out.println(list.displayName);
		System.out.println(list.webUrl);
		System.out.println(list.name);
//		System.out.println(list.drive);
//		System.out.println(list.drive.items);
//		System.out.println(list.drive.items.getCurrentPage());
		
		List<ListItem> listItems = list.items.getCurrentPage();
		com.microsoft.graph.requests.ListItemCollectionRequestBuilder listItems1 = list.items.getNextPage();
		System.out.println(listItems1);
		System.out.println(listItems1);
		
		ListItem item = listItems.stream()
				.filter(allFiles -> allFiles.fields.additionalDataManager().containsValue(new JsonPrimitive(vaultSearch)))
				.findAny().orElse(null);
		if (null != item) {
			System.out.println("item found");
			System.out.println(item.webUrl);
			System.out.println(item.id);
			System.out.println(item.eTag);
		} else {
			System.out.println("no item found");

		}

	}

	public Object getFileMac() throws Exception {
		String vaultSearch = "EndFile_231231"; //maria_1234 imanage_1234 EndFile_231231
		String siteId = "7tp7r4.sharepoint.com,01c01712-1760-4955-b9a8-0c021bb84186,ee8609f0-c8c7-4132-9ce9-bb4a2b998b27";
//		String driveId = "b!EhfAAWAXVUm5qAwCG7hBhvAJhu7HyDJBnOm7SiuZiye6YdI-0M4iT6vljGfNedy8";
		
		GraphServiceClient graphClient = new AuthenticationProvider().getAuthProvider();

		DriveItemSearchCollectionPage listCollectionPage = graphClient.sites(siteId).drive().root()
				.search(DriveItemSearchParameterSet.newBuilder().withQ(vaultSearch).build()).buildRequest().get();
		List<DriveItem> driveItems = listCollectionPage.getCurrentPage();
		if(driveItems.size() == 0) {
			System.out.println("no files");
			return null;
		}
		DriveItem driveItem = driveItems.get(0);
		
		String file_name = "";

//		Long size = additionalDataManager.get("FilesizeDisplay").getAsLong();

//		String filePath = listItem.webUrl.substring(driveItem.webUrl.length() + 1);
		System.out.println("filePath: " + driveItem.file);
		System.out.println("filePath: " + driveItem);
		String key = driveItem.id;
		JsonObject jsonObjectFields = (com.google.gson.JsonObject) graphClient
				.customRequest("/sites/" + siteId + "/drive/items/" + key +"/listItem?expand=fields").buildRequest().get();
		
		JsonObject jsonElement = jsonObjectFields.get("fields").getAsJsonObject();
		if (jsonElement.get("FileName") != null) {
			file_name = jsonElement.get("FileName").getAsString();
		} else {
			file_name = jsonElement.get("LinkFilename").getAsString();
		}
		
		String mimeType = driveItem.file.mimeType;
		
//		JsonObject jsonElementFields = jsonObjectFields.get("file").getAsJsonObject();
//		String mimeType = jsonElementFields.get("mimeType").getAsString();
//		String key = jsonObjectFields.get("id").getAsString();
		InputStream responseInputStream = (InputStream) graphClient
				.customRequest("/sites/" + siteId + "/drive/items/" + key + "/content", InputStream.class).buildRequest()
				.get();
		final String name = URLDecoder.decode(file_name, StandardCharsets.UTF_8);
		System.out.println("Downloading: end" + file_name);
		//DocumentInputStream.builder().type(mimeType).size(size).key(key).name(name).inputStream(responseInputStream).build();

		return null;
	}
	
	public Object getFileMacOriginal() throws Exception {
		String vaultSearch = "imanage_1234"; //maria_1234 imanage_1234
		String siteId = "7tp7r4.sharepoint.com,01c01712-1760-4955-b9a8-0c021bb84186,ee8609f0-c8c7-4132-9ce9-bb4a2b998b27";
		String driveId = "b!EhfAAWAXVUm5qAwCG7hBhvAJhu7HyDJBnOm7SiuZiye6YdI-0M4iT6vljGfNedy8";
		
		GraphServiceClient graphClient = new AuthenticationProvider().getAuthProvider();
		LinkedList<Option> requestOptions = new LinkedList<Option>();
		requestOptions.add(new QueryOption("expand", "items(expand=fields)"));
		ListCollectionPage listCollectionPage = graphClient.sites(siteId).lists()
				.buildRequest(requestOptions).filter("displayName eq 'Documents'").get();
		com.microsoft.graph.models.List driveItem = listCollectionPage.getCurrentPage().get(0);
		String listId = driveItem.id;
		List<ListItem> listItems = driveItem.items.getCurrentPage();
		ListItem listItem = listItems.stream().filter(
				allfiles -> allfiles.fields.additionalDataManager().containsValue(new JsonPrimitive(vaultSearch)))
				.findAny().orElse(null);

		if (null == listItem) {
			System.out.println("No Document found in SharePoint for Worksite Document ID: " + vaultSearch);
			return null;
		}
		AdditionalDataManager additionalDataManager = listItem.fields.additionalDataManager();
		String file_name = "";
		if (additionalDataManager.get("FileName") != null) {
			file_name = additionalDataManager.get("FileName").getAsString();
		} else {
			file_name = additionalDataManager.get("LinkFilename").getAsString();
		}

//		Long size = additionalDataManager.get("FilesizeDisplay").getAsLong();

		String filePath = listItem.webUrl.substring(driveItem.webUrl.length() + 1);
		System.out.println("filePath: " + filePath);
		JsonObject jsonObjectFields = (com.google.gson.JsonObject) graphClient
				.customRequest("/sites/" + siteId + "/drives/" + driveId + "/root:/" + filePath).buildRequest().get();

		JsonObject jsonElementFields = jsonObjectFields.get("file").getAsJsonObject();
		String mimeType = jsonElementFields.get("mimeType").getAsString();
		String key = jsonObjectFields.get("id").getAsString();
		InputStream responseInputStream = (InputStream) graphClient
				.customRequest("/sites/" + siteId + "/drive/items/" + key + "/content", InputStream.class).buildRequest()
				.get();
		final String name = URLDecoder.decode(file_name, StandardCharsets.UTF_8);
		System.out.println("Downloading: end" + file_name);
		//DocumentInputStream.builder().type(mimeType).size(size).key(key).name(name).inputStream(responseInputStream).build();

		return null;
	}

}
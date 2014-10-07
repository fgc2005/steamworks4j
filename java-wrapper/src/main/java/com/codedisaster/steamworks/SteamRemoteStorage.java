package com.codedisaster.steamworks;

import java.nio.ByteBuffer;

public class SteamRemoteStorage extends SteamInterface {

	public enum UGCReadAction {
		ContinueReadingUntilFinished,
		ContinueReading,
		Close
	}

	public enum PublishedFileVisibility {
		Public,
		FriendsOnly,
		Private
	}

	public enum WorkshopFileType {
		Community
	}

	public SteamRemoteStorage(long pointer, SteamRemoteStorageCallback callback) {
		super(pointer);
		registerCallback(new SteamRemoteStorageCallbackAdapter(callback));
	}

	static void dispose() {
		registerCallback(null);
	}

	public boolean fileWrite(String name, ByteBuffer data, int length) throws SteamException {
		if (!data.isDirect()) {
			throw new SteamException("Buffer must be direct!");
		}
		return fileWrite(pointer, name, data, length);
	}

	public boolean fileRead(String name, ByteBuffer buffer, int capacity) throws SteamException {
		if (!buffer.isDirect()) {
			throw new SteamException("Buffer must be direct!");
		}
		return fileRead(pointer, name, buffer, capacity);
	}

	public boolean fileDelete(String name) {
		return fileDelete(pointer, name);
	}

	public SteamAPICall fileShare(String name) {
		return new SteamAPICall(fileShare(pointer, name));
	}

	public SteamUGCFileWriteStreamHandle fileWriteStreamOpen(String name) {
		return new SteamUGCFileWriteStreamHandle(fileWriteStreamOpen(pointer, name));
	}

	public boolean fileWriteStreamWriteChunk(SteamUGCFileWriteStreamHandle stream, ByteBuffer data, int length) {
		return fileWriteStreamWriteChunk(pointer, stream.handle, data, length);
	}

	public boolean fileWriteStreamClose(SteamUGCFileWriteStreamHandle stream) {
		return fileWriteStreamClose(pointer, stream.handle);
	}

	public boolean fileWriteStreamCancel(SteamUGCFileWriteStreamHandle stream) {
		return fileWriteStreamCancel(pointer, stream.handle);
	}

	public boolean fileExists(String name) {
		return fileExists(pointer, name);
	}

	public int getFileSize(String name) {
		return getFileSize(pointer, name);
	}

	public int getFileCount() {
		return getFileCount(pointer);
	}

	public String getFileNameAndSize(int index, int[] sizes) {
		return getFileNameAndSize(pointer, index, sizes);
	}

	public SteamAPICall ugcDownload(SteamUGCHandle fileHandle, int priority) {
		return new SteamAPICall(ugcDownload(pointer, fileHandle.handle, priority));
	}

	public int ugcRead(SteamUGCHandle fileHandle, ByteBuffer buffer, int capacity, int offset, UGCReadAction action) {
		return ugcRead(pointer, fileHandle.handle, buffer, capacity, offset, action.ordinal());
	}

	public SteamAPICall publishWorkshopFile(String file, String previewFile,
										   long consumerAppID, String title, String description,
										   PublishedFileVisibility visibility, String[] tags,
										   WorkshopFileType workshopFileType) {

		return new SteamAPICall(publishWorkshopFile(pointer, file, previewFile, consumerAppID, title,
				description, visibility.ordinal(), tags, tags != null ? tags.length : 0, workshopFileType.ordinal()));
	}

	public SteamPublishedFileUpdateHandle createPublishedFileUpdateRequest(SteamPublishedFileID publishedFileID) {
		return new SteamPublishedFileUpdateHandle(createPublishedFileUpdateRequest(pointer, publishedFileID.id));
	}

	public boolean updatePublishedFileFile(SteamPublishedFileUpdateHandle updateHandle, String file) {
		return updatePublishedFileFile(pointer, updateHandle.handle, file);
	}

	public boolean updatePublishedFilePreviewFile(SteamPublishedFileUpdateHandle updateHandle, String previewFile) {
		return updatePublishedFilePreviewFile(pointer, updateHandle.handle, previewFile);
	}

	public boolean updatePublishedFileTitle(SteamPublishedFileUpdateHandle updateHandle, String title) {
		return updatePublishedFileTitle(pointer, updateHandle.handle, title);
	}

	public boolean updatePublishedFileDescription(SteamPublishedFileUpdateHandle updateHandle, String description) {
		return updatePublishedFileDescription(pointer, updateHandle.handle, description);
	}

	public SteamAPICall commitPublishedFileUpdate(SteamPublishedFileUpdateHandle updateHandle) {
		return new SteamAPICall(commitPublishedFileUpdate(pointer, updateHandle.handle));
	}

	/*JNI
		#include <steam_api.h>
		#include "SteamRemoteStorageCallback.h"

		static SteamRemoteStorageCallback* callback = NULL;
	*/

	static private native boolean registerCallback(SteamRemoteStorageCallbackAdapter javaCallback); /*
		if (callback != NULL) {
			delete callback;
			callback = NULL;
		}

		if (javaCallback != NULL) {
			callback = new SteamRemoteStorageCallback(env, javaCallback);
		}

		return callback != NULL;
	*/

	static private native boolean fileWrite(long pointer, String name, ByteBuffer data, int length); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileWrite(name, data, length);
	*/

	static private native boolean fileRead(long pointer, String name, ByteBuffer buffer, int capacity); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileRead(name, buffer, capacity);
	*/

	static private native boolean fileDelete(long pointer, String name); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileDelete(name);
	*/

	static private native long fileShare(long pointer, String name); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		SteamAPICall_t handle = storage->FileShare(name);
		callback->onFileShareResultCall.Set(handle, callback, &SteamRemoteStorageCallback::onFileShareResult);
		return handle;
	*/

	static private native long fileWriteStreamOpen(long pointer, String name); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileWriteStreamOpen(name);
	*/

	static private native boolean fileWriteStreamWriteChunk(long pointer, long stream, ByteBuffer data, int length); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileWriteStreamWriteChunk((UGCFileWriteStreamHandle_t) stream, data, length);
	*/

	static private native boolean fileWriteStreamClose(long pointer, long stream); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileWriteStreamClose((UGCFileWriteStreamHandle_t) stream);
	*/

	static private native boolean fileWriteStreamCancel(long pointer, long stream); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileWriteStreamCancel((UGCFileWriteStreamHandle_t) stream);
	*/

	static private native boolean fileExists(long pointer, String name); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->FileExists(name);
	*/

	static private native int getFileSize(long pointer, String name); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->GetFileSize(name);
	*/

	static private native int getFileCount(long pointer); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->GetFileCount();
	*/

	static private native String getFileNameAndSize(long pointer, int index, int[] sizes); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		jstring name = env->NewStringUTF(storage->GetFileNameAndSize(index, &sizes[0]));
		return name;
	*/

	static private native long ugcDownload(long pointer, long content, int priority); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		SteamAPICall_t handle = storage->UGCDownload(content, priority);
		callback->onDownloadUGCResultCall.Set(handle, callback, &SteamRemoteStorageCallback::onDownloadUGCResult);
		return handle;
	*/

	static private native int ugcRead(long pointer, long content, ByteBuffer buffer, int capacity,
									  int offset, int action); /*

		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->UGCRead(content, buffer, capacity, offset, (EUGCReadAction) action);
	*/

	static private native long publishWorkshopFile(long pointer, String file, String previewFile, long consumerAppID,
												   String title, String description, int visibility, String[] tags,
												   int numTags, int workshopFileType); /*

		SteamParamStringArray_t arrayTags;
		arrayTags.m_ppStrings = (numTags > 0) ? new const char*[numTags] : NULL;
		arrayTags.m_nNumStrings = numTags;
		for (int t = 0; t < numTags; t++) {
			arrayTags.m_ppStrings[t] = env->GetStringUTFChars((jstring) env->GetObjectArrayElement(tags, t), 0);
		}

		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		SteamAPICall_t handle = storage->PublishWorkshopFile(file, previewFile, consumerAppID, title, description,
			(ERemoteStoragePublishedFileVisibility) visibility, &arrayTags, (EWorkshopFileType) workshopFileType);

		callback->onPublishFileResultCall.Set(handle, callback, &SteamRemoteStorageCallback::onPublishFileResult);

		for (int t = 0; t < numTags; t++) {
			env->ReleaseStringUTFChars((jstring) env->GetObjectArrayElement(tags, t), arrayTags.m_ppStrings[t]);
		}
		delete[] arrayTags.m_ppStrings;

		return handle;
	*/

	static private native long createPublishedFileUpdateRequest(long pointer, long publishedFileID); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->CreatePublishedFileUpdateRequest(publishedFileID);
	*/

	static private native boolean updatePublishedFileFile(long pointer, long updateHandle, String file); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->UpdatePublishedFileFile(updateHandle, file);
	*/

	static private native boolean updatePublishedFilePreviewFile(long pointer, long updateHandle, String previewFile); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->UpdatePublishedFilePreviewFile(updateHandle, previewFile);
	*/

	static private native boolean updatePublishedFileTitle(long pointer, long updateHandle, String title); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->UpdatePublishedFileTitle(updateHandle, title);
	*/

	static private native boolean updatePublishedFileDescription(long pointer, long updateHandle, String description); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		return storage->UpdatePublishedFileDescription(updateHandle, description);
	*/

	static private native long commitPublishedFileUpdate(long pointer, long updateHandle); /*
		ISteamRemoteStorage* storage = (ISteamRemoteStorage*) pointer;
		SteamAPICall_t handle = storage->CommitPublishedFileUpdate(updateHandle);

		callback->onUpdatePublishedFileResultCall.Set(handle, callback, &SteamRemoteStorageCallback::onUpdatePublishedFileResult);

		return handle;
	*/

}

package com.kelvinapps.rxfirebase;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;

import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nick Moskalenko on 25/05/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class RxFirebaseStorageTests {

    @Mock
    private StorageReference mockStorageRef;

    @Mock
    private Task<Void> mockVoidTask;

    @Mock
    private Task<byte[]> mockBytesTask;

    @Mock
    private Task<Uri> mockUriTask;

    @Mock
    private FileDownloadTask mockFileDownloadTask;

    @Mock
    private StreamDownloadTask mockStreamDownloadTask;

    @Mock
    private Task<StorageMetadata> mockMetadataTask;

    @Mock
    private UploadTask mockUploadTask;

    @Mock
    private Uri uri;

    @Mock
    private File file;

    @Mock
    private StorageMetadata metadata;

    @Mock
    private FileDownloadTask.TaskSnapshot fileSnapshot;

    @Mock
    private StreamDownloadTask.TaskSnapshot streamSnapshot;

    @Mock
    private UploadTask.TaskSnapshot uploadSnapshot;

    @Mock
    private StreamDownloadTask.StreamProcessor processor;

    @Mock
    private InputStream stream;

    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Captor
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;

    @Captor
    private ArgumentCaptor<OnFailureListener> testOnFailureListener;

    private byte[] bytes;
    private Void voidData = null;

    @Before
    public void setup() {
        setupTask(mockBytesTask);
        setupTask(mockVoidTask);
        setupTask(mockUriTask);
        setupTask(mockFileDownloadTask);
        setupTask(mockStreamDownloadTask);
        setupTask(mockMetadataTask);
        setupTask(mockUploadTask);

        when(mockStorageRef.getBytes(20)).thenReturn(mockBytesTask);
        when(mockStorageRef.getDownloadUrl()).thenReturn(mockUriTask);
        when(mockStorageRef.getFile(file)).thenReturn(mockFileDownloadTask);
        when(mockStorageRef.getFile(uri)).thenReturn(mockFileDownloadTask);
        when(mockStorageRef.getStream()).thenReturn(mockStreamDownloadTask);
        when(mockStorageRef.getStream(processor)).thenReturn(mockStreamDownloadTask);
        when(mockStorageRef.getMetadata()).thenReturn(mockMetadataTask);
        when(mockStorageRef.putBytes(bytes)).thenReturn(mockUploadTask);
        when(mockStorageRef.putBytes(bytes, metadata)).thenReturn(mockUploadTask);
        when(mockStorageRef.putFile(uri)).thenReturn(mockUploadTask);
        when(mockStorageRef.putFile(uri, metadata)).thenReturn(mockUploadTask);
        when(mockStorageRef.putFile(uri, metadata, uri)).thenReturn(mockUploadTask);
        when(mockStorageRef.putStream(stream)).thenReturn(mockUploadTask);
        when(mockStorageRef.putStream(stream, metadata)).thenReturn(mockUploadTask);
        when(mockStorageRef.updateMetadata(metadata)).thenReturn(mockMetadataTask);
        when(mockStorageRef.delete()).thenReturn(mockVoidTask);
    }


    private <T> void setupTask(Task<T> task) {
        when(task.addOnCompleteListener(testOnCompleteListener.capture())).thenReturn(task);
        when(task.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(task);
        when(task.addOnFailureListener(testOnFailureListener.capture())).thenReturn(task);
    }

    @Test
    public void getBytes() {

        TestSubscriber<byte[]> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getBytes(mockStorageRef, 20)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(bytes);
        testOnCompleteListener.getValue().onComplete(mockBytesTask);

        verify(mockStorageRef).getBytes(20);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(bytes));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getDownloadUrl() {

        TestSubscriber<Uri> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getDownloadUrl(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uri);
        testOnCompleteListener.getValue().onComplete(mockUriTask);

        verify(mockStorageRef).getDownloadUrl();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uri));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getFile() {

        TestSubscriber<FileDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getFile(mockStorageRef, file)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(fileSnapshot);

        verify(mockStorageRef).getFile(file);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(fileSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getFileUri() {

        TestSubscriber<FileDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getFile(mockStorageRef, uri)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(fileSnapshot);

        verify(mockStorageRef).getFile(uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(fileSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }


    @Test
    public void getMetadata() {

        TestSubscriber<StorageMetadata> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getMetadata(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(metadata);
        testOnCompleteListener.getValue().onComplete(mockMetadataTask);

        verify(mockStorageRef).getMetadata();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(metadata));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }


    @Test
    public void getStream() {

        TestSubscriber<StreamDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getStream(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(streamSnapshot);

        verify(mockStorageRef).getStream();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(streamSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void getStreamProcessor() {

        TestSubscriber<StreamDownloadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.getStream(mockStorageRef, processor)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(streamSnapshot);

        verify(mockStorageRef).getStream(processor);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(streamSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putBytes() {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putBytes(mockStorageRef, bytes)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putBytes(bytes);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putBytesMetadata() {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putBytes(mockStorageRef, bytes, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putBytes(bytes, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putFile() {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putFile(uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putFileMetadata() {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putFile(uri, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putFileMetadataAndUri() {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putFile(mockStorageRef, uri, metadata, uri)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putFile(uri, metadata, uri);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putStream() {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putStream(mockStorageRef, stream)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putStream(stream);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void putStreamMetadata() {

        TestSubscriber<UploadTask.TaskSnapshot> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.putStream(mockStorageRef, stream, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(uploadSnapshot);

        verify(mockStorageRef).putStream(stream, metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(uploadSnapshot));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void updateMetadata() {

        TestSubscriber<StorageMetadata> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.updateMetadata(mockStorageRef, metadata)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(metadata);

        verify(mockStorageRef).updateMetadata(metadata);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(metadata));
        testSubscriber.assertNotCompleted();
        testSubscriber.unsubscribe();
    }

    @Test
    public void delete() {

        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        RxFirebaseStorage.delete(mockStorageRef)
                .subscribeOn(Schedulers.immediate())
                .subscribe(testSubscriber);

        testOnSuccessListener.getValue().onSuccess(voidData);
        testOnCompleteListener.getValue().onComplete(mockVoidTask);

        verify(mockStorageRef).delete();

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertReceivedOnNext(Collections.singletonList(voidData));
        testSubscriber.assertCompleted();
        testSubscriber.unsubscribe();
    }


}

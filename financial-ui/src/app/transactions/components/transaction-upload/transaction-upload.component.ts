import { Component, ElementRef, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpEvent, HttpEventType } from '@angular/common/http';
import { RippleModule } from 'primeng/ripple';
import { ProgressBarModule } from 'primeng/progressbar';


interface UploadEvent {
  originalEvent: HttpEvent<any>;
  files: File[];
}

interface UploadProgress {
  status: number;
  currentProgress: String;
}

@Component({
  selector: 'app-transaction-upload',
  standalone: true,
  imports: [ButtonModule, RippleModule,ProgressBarModule, CommonModule],
  templateUrl: './transaction-upload.component.html',
  styleUrl: './transaction-upload.component.scss'
})
export class TransactionUploadComponent {

  uploadedFiles: any[] = [];
  showProgressBar = false;
  progressValue = 0;
  @ViewChild('uploader') uploader!: ElementRef;
  constructor(private http: HttpClient) {}
  
  whenUploadFile(event: UploadEvent) {
    console.log(event + "helloo..")
    for(let file of event.files) {
        this.uploadedFiles.push(file);
    }

  }

  startProgress() {
    this.showProgressBar = true;
    this.progressValue = 0;

    this.uploader.nativeElement.click();

    // const interval = setInterval(() => {
    //   this.progressValue += 10;
    //   if (this.progressValue >= 100) {
    //     clearInterval(interval);
    //     this.showProgressBar = false;
    //   }
    // }, 500);
  }


  onFileSelected(event: any) {
    const files: File[] = event.target.files;
    this.uploadFiles(files);
  }


  uploadFiles(files: File[]) {
    const formData = new FormData();
    for (let file of files) {
      formData.append('file', file, file.name);
    }
    formData.append('type', "icici");

    this.http.post('http://localhost:8090/bank/statement/upload', formData, {
      reportProgress: true,
      observe: 'events'
    }).subscribe((event: HttpEvent<any>) => {
      switch (event.type) {
        case HttpEventType.UploadProgress:
          if (event.total) {
            this.progressValue = Math.round(100 * event.loaded / event.total);
          }
          break;
        case HttpEventType.Response:
          console.log('Files uploaded successfully!', event.body);
          this.showProgressBar = false;
          this.listenToSse();
          break;
      }
    }, error => {
      console.error('File upload failed!', error);
      this.showProgressBar = false;
    });
  }


  listenToSse() {
    const eventSource = new EventSource('http://localhost:8090/bank/statement/upload/progress');
    eventSource.onmessage = (event) => {
      const uploadProgress: UploadProgress = JSON.parse(event.data);
      this.progressValue = uploadProgress.status;
      console.log(uploadProgress.currentProgress);
      if (uploadProgress.status >= 100) {
        eventSource.close();
        this.showProgressBar = false;
      }
    };
    eventSource.onerror = (error) => {
      console.error('SSE error:', error);
      eventSource.close();
      this.showProgressBar = false;
    };
  }

}

import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import {FileUpload} from 'primeng/fileupload';
import { CommonModule } from '@angular/common';
import { BadgeModule } from 'primeng/badge';
import { ProgressBarModule } from 'primeng/progressbar';
import { ToastModule } from 'primeng/toast';
import { HttpEvent } from '@angular/common/http';

interface UploadEvent {
  originalEvent: HttpEvent<any>;
  files: File[];
}

@Component({
  selector: 'app-transaction-upload',
  standalone: true,
  imports: [ButtonModule, FileUpload, BadgeModule, ToastModule, ProgressBarModule, CommonModule],
  templateUrl: './transaction-upload.component.html',
  styleUrl: './transaction-upload.component.scss'
})
export class TransactionUploadComponent {

  uploadedFiles: any[] = [];

  whenUploadFile(event: UploadEvent) {
    console.log(event + "helloo..")
    for(let file of event.files) {
        this.uploadedFiles.push(file);
    }

}

}

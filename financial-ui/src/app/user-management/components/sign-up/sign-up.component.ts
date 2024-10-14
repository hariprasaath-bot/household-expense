import { Component, ElementRef, ViewChild } from '@angular/core';
import { GuidingTorchComponent } from "../../../common/components/guiding-torch/guiding-torch.component";
import { BoilingEffectComponent } from '../../../common/components/boiling-effect/boiling-effect.component';
import { ProgressLightComponent } from "../../../common/components/progress-light/progress-light.component";
import { ProgressLightServiceService } from '../../../common/service/progress-light-service.service';
import { CommonModule } from '@angular/common'
import { SignUpFormsComponent } from "../../containers/sign-up-forms/sign-up-forms.component";

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [GuidingTorchComponent, BoilingEffectComponent, ProgressLightComponent, CommonModule, SignUpFormsComponent],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss'
})
export class SignUpComponent {
  @ViewChild('videoPlayer') videoPlayer!: ElementRef<HTMLVideoElement>;
  heatingSource :boolean = false;
  interval :any;
  boiled :boolean = false;


  constructor(private progressService: ProgressLightServiceService) { }
  
  ngOnInit(){
    this.progressService.startLoading();
    this.progressService.progress$.subscribe(value => {
      this.boiled = value > 0; // Set boiled to true if progress is greater than 0
      if(this.boiled){
        this.videoPlayer.nativeElement.play()
      }else{
        this.videoPlayer.nativeElement.pause()
        this.videoPlayer.nativeElement.currentTime = 0;
      }
    });
  }

  onHover() {
    this.heatingSource = true;
    clearInterval(this.interval);
    
      this.interval = setInterval(() => {
        this.progressService.setProgress(this.progressService.progressSubject.value + 10);
        if (this.progressService.progressSubject.value >= 100) {
          clearInterval(this.interval);
        }
      }, 700);
  }

  onLeave() {
    this.heatingSource = false;
    clearInterval(this.interval);
    this.interval = setInterval(() => {
      this.progressService.decreaseProgress();
      if (this.progressService.progressSubject.value <= 0) {
        clearInterval(this.interval);
      }
    }, 350);
  }
}

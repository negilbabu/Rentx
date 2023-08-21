import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import KeenSlider, { KeenSliderInstance, KeenSliderPlugin } from 'keen-slider';

//keenSlider config.
function ThumbnailPlugin(main: KeenSliderInstance): KeenSliderPlugin {
  return (slider) => {
    function removeActive() {
      slider.slides.forEach((slide) => {
        slide.classList.remove('active');
      });
    }
    function addActive(idx: number) {
      slider.slides[idx].classList.add('active');
    }

    function addClickEvents() {
      slider.slides.forEach((slide, idx) => {
        slide.addEventListener('click', () => {
          main.moveToIdx(idx);
        });
      });
    }

    slider.on('created', () => {
      addActive(slider.track.details.rel);
      addClickEvents();
      main.on('animationStarted', (main) => {
        removeActive();
        const next = main.animator.targetIdx ?? 0;
        addActive(main.track.absToRel(next));
        slider.moveToIdx(Math.min(slider.track.details.maxIdx, next));
      });
    });
  };
}

@Component({
  selector: 'app-image-gallery',
  templateUrl: './image-gallery.component.html',
  styleUrls: ['./image-gallery.component.css'],
})
export class ImageGalleryComponent implements AfterViewInit {
  @ViewChild('sliderRef') sliderRef!: ElementRef<HTMLElement>;
  @ViewChild('thumbnailRef') thumbnailRef!: ElementRef<HTMLElement>;
  slider!: KeenSliderInstance;
  thumbnailSlider!: KeenSliderInstance;
  currentSlide: number = 0;

  @Input() productImageList: any;

  ngOnChanges(changes: SimpleChanges): void {
    if (
      changes['productImageList'] &&
      !changes['productImageList'].firstChange
    ) {
      this.destroySliders();

      this.initializeSliders();
    }
  }

  ngAfterViewInit() {
    if (this.sliderRef && this.thumbnailRef) {
      this.initializeSliders();
    }
  }

  private destroySliders() {
    if (this.slider) {
      this.slider.destroy();
    }
    if (this.thumbnailSlider) {
      this.thumbnailSlider.destroy();
    }
  }

  private initializeSliders() {
    this.slider = new KeenSlider(this.sliderRef.nativeElement);
    this.thumbnailSlider = new KeenSlider(
      this.thumbnailRef.nativeElement,
      {
        slideChanged: (s) => {
          this.currentSlide = s.track.details.rel;
        },
        initial: this.currentSlide,
        slides: {
          perView: 4,
          spacing: 2,
        },
      },
      [ThumbnailPlugin(this.slider)]
    );
  }

  ngOnDestroy() {
    this.destroySliders();
  }

  getImageUrl(image: any, index: number): string {
    const imageKey = 'image' + (index + 1);
    return image[imageKey];
  }
}

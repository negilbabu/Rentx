import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavComponent } from './components/nav/nav.component';
import { HomeComponent } from './components/home/home.component';
import { OtpVerifyComponent } from './components/otp-verify/otp-verify.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { FooterComponent } from './components/footer/footer.component';
import { SharedModule } from '../shared/shared.module';
import { VendorSidebarComponent } from './components/vendor-sidebar/vendor-sidebar.component';
import { CoreRoutingModule } from './core-routing.module';
import { BlockedComponent } from './components/blocked/blocked.component';
import { BannerComponent } from './components/banner/banner.component';
import { NavVendorComponent } from './components/nav-vendor/nav-vendor.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ImageGalleryComponent } from './components/image-gallery/image-gallery.component';
import { ErrorPageComponent } from './components/error-page/error-page.component';
import { UserSideBarComponent } from './components/user-side-bar/user-side-bar.component';

@NgModule({
  declarations: [
    NavComponent,
    HomeComponent,
    OtpVerifyComponent,
    FooterComponent,
    VendorSidebarComponent,
    BlockedComponent,
    BannerComponent,
    NavVendorComponent,
    ImageGalleryComponent,
    ErrorPageComponent,
    UserSideBarComponent,
  ],
  imports: [
    CommonModule,
    MatTooltipModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatSnackBarModule,
    CoreRoutingModule,
    SharedModule,
    FormsModule,
  ],
  exports: [
    NavComponent,
    OtpVerifyComponent,
    HomeComponent,
    FooterComponent,
    VendorSidebarComponent,
    BannerComponent,
    NavVendorComponent,
    ImageGalleryComponent,
    UserSideBarComponent,
  ],
})
export class CoreModule {}

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AlertModule } from './components/core/alert/alert.module';
import { RecaptchaV3Module, RECAPTCHA_V3_SITE_KEY  } from 'ng-recaptcha';
import { NgxPaginationModule } from 'ngx-pagination'; 
import { AngularEditorModule } from '@kolkov/angular-editor';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ResetPasswordComponent } from './components/auth/reset-password/reset-password.component';
import { RecoverPasswordComponent } from './components/auth/recover-password/recover-password.component';
import { HomeComponent } from './components/home/home.component';

import { HttpRequestInterceptor } from './shared/interceptors/HttpRequestInterceptor.interceptor';
import { TokenErrorInterceptor } from './shared/interceptors/TokenErrorInterceptor.interceptor';
import { DatasetsListComponent } from './components/dataset/datasets-list/datasets-list.component';
import { DatasetDetailsComponent } from './components/dataset/dataset-details/dataset-details.component';
import { DatasetEditComponent } from './components/dataset/dataset-edit/dataset-edit.component';
import { DatasetCreateComponent } from './components/dataset/dataset-create/dataset-create.component';
import { HeaderComponent } from './components/core/header/header.component';
import { FooterComponent } from './components/core/footer/footer.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { VocabularyListComponent } from './components/vocabulary/vocabulary-list/vocabulary-list.component';
import { VocabularyCreateComponent } from './components/vocabulary/vocabulary-create/vocabulary-create.component';
import { VocabularyEditComponent } from './components/vocabulary/vocabulary-edit/vocabulary-edit.component';
import { NotFoundComponent } from './components/core/not-found/not-found.component';
import { DatasetDescriptionsComponent } from './components/dataset/dataset-descriptions/dataset-descriptions.component';
import { UsersListComponent } from './components/users/users-list/users-list.component';
import { UsersProfileComponent } from './components/users/users-profile/users-profile.component';
import { CatalogueListComponent } from './components/catalogue/catalogue-list/catalogue-list.component';
import { CatalogueDetailsComponent } from './components/catalogue/catalogue-details/catalogue-details.component';
import { SocioeconomicAnalysisUnitModalComponent } from './components/modals/socioeconomic-analysis-unit-modal/socioeconomic-analysis-unit-modal.component';
import { GeoreferencedAnalysisUnitModalComponent } from './components/modals/georeferenced-analysis-unit-modal/georeferenced-analysis-unit-modal.component';
import { PriceDatasetVariableModalComponent } from './components/modals/price-dataset-variable-modal/price-dataset-variable-modal.component';
import { DatasetVariableModalComponent } from './components/modals/dataset-variable-modal/dataset-variable-modal.component';
import { DistributionModalComponent } from './components/modals/distribution-modal/distribution-modal.component';
import { DataServiceModalComponent } from './components/modals/data-service-modal/data-service-modal.component';
import { VariableTableComponent } from './components/dataset/dataset-details/variable-table/variable-table.component';
import { DistributionTableComponent } from './components/dataset/dataset-details/distribution-table/distribution-table.component';
import { AnalysisUnitTableComponent } from './components/dataset/dataset-details/analysis-unit-table/analysis-unit-table.component';
import { DistributionDetailsModalComponent } from './components/modals/distribution-details-modal/distribution-details-modal.component';
import { AnalysisUnitDetailsModalComponent } from './components/modals/analysis-unit-details-modal/analysis-unit-details-modal.component';
import { DatasetVariableDetailsModalComponent } from './components/modals/dataset-variable-details-modal/dataset-variable-details-modal.component';
import { VocabularyValueModalComponent } from './components/modals/vocabulary-value-modal/vocabulary-value-modal.component';
import { RoleDirective } from './shared/directives/role.directive';
import { CommentListComponent } from './components/comment/comment-list/comment-list.component';
import { NewCommentBoxComponent } from './components/comment/new-comment-box/new-comment-box.component';
import { CommentReplyBoxComponent } from './components/comment/comment-reply-box/comment-reply-box.component';
import { DeleteCommentDirective } from './shared/directives/delete-comment.directive';
import { PurgeCommentDirective } from './shared/directives/purge-comment.directive';
import { EditDatasetDirective } from './shared/directives/edit-dataset.directive';
import { DeleteDatasetDirective } from './shared/directives/delete-dataset.directive';
import { ConfirmAccountComponent } from './components/auth/confirm-account/confirm-account.component';
import { ContactComponent } from './components/contact/contact.component';
import { UpdateCommentDirective } from './shared/directives/update-comment.directive';
import { CommentUpdateBoxComponent } from './components/comment/comment-update-box/comment-update-box.component';
import { HelpComponent } from './components/help/help.component';

import { environment } from 'src/environments/environment';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    RecoverPasswordComponent,
    ResetPasswordComponent,
    HomeComponent,
    DatasetsListComponent,
    DatasetDetailsComponent,
    DatasetEditComponent,
    DatasetCreateComponent,
    HeaderComponent,
    FooterComponent,
    VocabularyListComponent,
    VocabularyCreateComponent,
    VocabularyEditComponent,
    NotFoundComponent,
    DatasetDescriptionsComponent,
    UsersListComponent,
    UsersProfileComponent,
    CatalogueListComponent,
    CatalogueDetailsComponent,
    SocioeconomicAnalysisUnitModalComponent,
    GeoreferencedAnalysisUnitModalComponent,
    PriceDatasetVariableModalComponent,
    DatasetVariableModalComponent,
    DistributionModalComponent,
    DataServiceModalComponent,
    VariableTableComponent,
    DistributionTableComponent,
    AnalysisUnitTableComponent,
    DistributionDetailsModalComponent,
    AnalysisUnitDetailsModalComponent,
    DatasetVariableDetailsModalComponent,
    VocabularyValueModalComponent,
    RoleDirective,
    CommentListComponent,
    NewCommentBoxComponent,
    CommentReplyBoxComponent,
    DeleteCommentDirective,
    PurgeCommentDirective,
    ConfirmAccountComponent,
    ContactComponent,
    EditDatasetDirective,
    DeleteDatasetDirective,
    UpdateCommentDirective,
    CommentUpdateBoxComponent,
    HelpComponent
  ],
  imports: [
    AngularEditorModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule,
    
    HttpClientModule,
    NgbModule,
    AlertModule,
    NgxPaginationModule,
    RecaptchaV3Module
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: TokenErrorInterceptor, multi: true },
    { provide: RECAPTCHA_V3_SITE_KEY, useValue: environment.recaptcha.siteKey}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

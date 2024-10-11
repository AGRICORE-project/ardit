import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ResetPasswordComponent } from './components/auth/reset-password/reset-password.component';
import { RecoverPasswordComponent } from './components/auth/recover-password/recover-password.component';
import { ConfirmAccountComponent } from './components/auth/confirm-account/confirm-account.component';
// import { HomeComponent } from './home/home.component';
import { UsersProfileComponent } from './components/users/users-profile/users-profile.component';
import { UsersListComponent } from './components/users/users-list/users-list.component';
import { DatasetsListComponent } from './components/dataset/datasets-list/datasets-list.component';
import { DatasetDetailsComponent } from './components/dataset/dataset-details/dataset-details.component';
import { DatasetCreateComponent } from './components/dataset/dataset-create/dataset-create.component';
import { DatasetEditComponent } from './components/dataset/dataset-edit/dataset-edit.component';
import { DatasetDescriptionsComponent } from './components/dataset/dataset-descriptions/dataset-descriptions.component';
import { CatalogueListComponent } from './components/catalogue/catalogue-list/catalogue-list.component';
import { CatalogueDetailsComponent } from './components/catalogue/catalogue-details/catalogue-details.component';
import { VocabularyListComponent } from './components/vocabulary/vocabulary-list/vocabulary-list.component';
import { VocabularyCreateComponent } from './components/vocabulary/vocabulary-create/vocabulary-create.component';
import { VocabularyEditComponent } from './components/vocabulary/vocabulary-edit/vocabulary-edit.component';
import { ContactComponent } from './components/contact/contact.component';
import { NotFoundComponent } from './components/core/not-found/not-found.component';
import { HelpComponent } from './components/help/help.component';

import { AuthGuard } from './shared/guards/AuthGuard';
import { RoleGuard } from './shared/guards/RoleGuard';
import { RoutesNames } from './shared/utils/routes.names';
import { Role } from './shared/utils/roles';

const routes: Routes = [

  //ROUTES BASED ON D1.9 ROLES TABLE

  //GUEST ACCESS
  {path: '', redirectTo: RoutesNames.DATASETS, pathMatch: 'full'},
  {path: RoutesNames.LOGIN, component: LoginComponent},
  {path: RoutesNames.REGISTER, component: RegisterComponent},
  {path: RoutesNames.RESET_PASSWORD, component: ResetPasswordComponent},
  {path: RoutesNames.RECOVER_PASSWORD, component: RecoverPasswordComponent},
  {path: RoutesNames.CONFIRM_ACCOUNT, component: ConfirmAccountComponent},
  {path: RoutesNames.CATALOGUES, component: CatalogueListComponent},
  {path: RoutesNames.CATALOGUES + '/:id', component: CatalogueDetailsComponent},
  {path: RoutesNames.CONTACT, component: ContactComponent},
  {path: RoutesNames.DATASETS, component: DatasetsListComponent},
  {path: RoutesNames.HELP, component: HelpComponent},
  
  
  //USER ACCESS
  {path: RoutesNames.DATASET_EDIT + '/:id', canActivate: [AuthGuard], component: DatasetEditComponent},
  {path: RoutesNames.DATASET_CREATE, canActivate: [AuthGuard], component: DatasetCreateComponent},
  {path: RoutesNames.DATASETS + '/:id', component: DatasetDetailsComponent}, // GUEST ACCES, THIS HAS TO GO BELOW THE CREATE ROUTE
  {path: RoutesNames.PROFILE, canActivate: [AuthGuard], component: UsersProfileComponent},
  
  //EDITOR ACCESS
  {path: RoutesNames.USERS, canActivate: [AuthGuard, RoleGuard], component: UsersListComponent, data: { roles: [Role.ADMIN, Role.MANTAINER, Role.EDITOR]}},
  {path: RoutesNames.VOCABULARIES, canActivate: [AuthGuard, RoleGuard], component: VocabularyListComponent, data: { roles: [Role.ADMIN, Role.MANTAINER, Role.EDITOR]}},
  {path: RoutesNames.VOCABULARY_CREATE, canActivate: [AuthGuard, RoleGuard], component: VocabularyCreateComponent, data: { roles: [Role.ADMIN, Role.MANTAINER, Role.EDITOR]}},
  {path: RoutesNames.VOCABULARY_EDIT + '/:id', canActivate: [AuthGuard, RoleGuard], component: VocabularyEditComponent, data: { roles: [Role.ADMIN, Role.MANTAINER,  Role.EDITOR]}},
  
  //MANTAINER ACCESS
  {path: RoutesNames.DATASETS_DESCRIPTIONS, canActivate: [AuthGuard, RoleGuard], component: DatasetDescriptionsComponent, data: { roles: [Role.ADMIN, Role.MANTAINER]}},
  
  //ADMIN ACCESS
  
  {path: RoutesNames.NOT_FOUND, component: NotFoundComponent },
  {path: '**', redirectTo: `/${RoutesNames.NOT_FOUND}` }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

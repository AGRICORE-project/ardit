import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { ReCaptchaV3Service } from 'ng-recaptcha';

import { AlertService } from 'src/app/services/alert/alert.service';
import { EmailService } from '../../services/email/email.service';
import { getContactTemplate } from '../../shared/utils/contact-temaplate'
import { EmailDTO } from '../../models/dto/email-dto';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent {

  // Spinner for submit
  public isSending: boolean = false;

  //Email body that will be sent to the receiver
  emailBody: string;

  // Contact form validators
  contactForm: FormGroup = this.formBuilder.group({
      name: [, [Validators.required, Validators.minLength(3)]],
      surname: [, [Validators.required, Validators.minLength(3)]],
      email: [, [Validators.required, Validators.email]],
      subject: [, [Validators.required, Validators.minLength(3)]],
      message: [, [Validators.required, Validators.minLength(10)]]
  })

  // Options for alert service
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(private formBuilder: FormBuilder, private emailService: EmailService, private alertService: AlertService, private recaptchaV3Service: ReCaptchaV3Service) { }

  get nameErrorMsg(): string {
    const errors = this.contactForm.get('name').errors;
    if (errors.required) return 'Name is a required field';
    if (errors.minlength) return 'Minium name length is 3 characters';
  }

  get surnameErrorMsg(): string {
    const errors = this.contactForm.get('surname').errors;
    if (errors.required) return 'Surname is a required field';
    if (errors.minlength) return 'Minium surname length is 3 characters';
  }

  get subjectErrorMsg(): string {
    const errors = this.contactForm.get('subject').errors;
    if (errors.required) return 'Subject is a required field';
  }

  get emailErrorMsg(): string {
    const errors = this.contactForm.get('email').errors;
    if (errors.required) return 'Email is a required field';
    if (errors.email) return 'Invalid email format';
  }

  get messageErrorMsg(): string {
    const errors = this.contactForm.get('message').errors;
    if (errors.required) return 'Message is a required field';
    if (errors.minlength) return 'Minium message length is 10 characters';
  }

 public inavlidField(field: string) {
      return this.contactForm.get(field).touched && this.contactForm.get(field).invalid;
  }
  /**
   * Submit and send email with form values
   */
  public handleSubmit(): void {
    
    this.recaptchaV3Service.execute('importantAction')
    .subscribe( (token: string) => 
      {
        if (token) {
          this.isSending = true

          this.emailBody = getContactTemplate(this.contactForm.value)
          const emailDTO: EmailDTO = {
            body: this.emailBody,
            subject: this.contactForm.value.subject,
            html: true
          }
      
          this.emailService.sendEmail(emailDTO)
          .pipe(finalize(() => {
            this.isSending = false
            this.contactForm.reset();
          } ))
          .subscribe(
            res => this.alertService.success(`Email was sent succesfully.`, this.options),
            err => this.alertService.error(`There was an error sending the email.`, this.options)
          ) 
        } else {
          this.alertService.error(`Invalid recaptcha token.`, this.options)
        }
      },
    );
  }
}

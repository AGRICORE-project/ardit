import { TestBed } from '@angular/core/testing';
import { HttpClientModule} from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';

import { AuthService } from './auth.service';
import { ApiService } from '../api/api.service';

describe('AuthService', async () => {
    let service: AuthService;

    const username = 'user1';
    const email = 'user1@mail.com';
    const password = 'user1';

    beforeEach(() => {
        TestBed.configureTestingModule({
        imports: [
            HttpClientModule,
            RouterTestingModule
        ],
        providers: [ApiService]});
        service = TestBed.inject(AuthService);
    });

    /*it('should register an user', (done) => {
        service.signUp(username, email, password).subscribe((res: any) => {
            expect(res.token).toBeDefined();
            done();
        });
    });*/

    /*it('should show an error if the user already exists', (done) => {
        service.signUp(username, email, password).subscribe((res: any) => {
            expect(res.error.message).toEqual('User already exists');
            done();
        });
    });*/

    it('should login an user', (done) => {
        service.signIn(username, password).subscribe((res: any) => {
            // expect(res.status).toEqual(200);
            expect(res.token).toBeDefined();
            expect(localStorage.getItem('currentUser')).toBeNull();
            done();
        });
    });

    it('should show an error if the login user does not exists', (done) => {
        const invalidUsername = 'notExistingUser';
        const invalidPassword = 'password';
        service.signIn(invalidUsername, invalidPassword).subscribe((res: any) => {
            expect(res.error.message).toEqual('Bad credentials');
        });
        done();
    });

    /*it('should logout an user', () => {
        service.logout().subscribe((res: any) => {
            expect(res.status).toEqual(204);
            // expect(res).toEqual(null);
        });
    });

    it('should get current user data', () => {
        service.getCurrentUser().subscribe((res: any) => {
            expect(res.status).toEqual(200);
            expect(res.username).toBe(username);
            expect(res.email).toBe(email);
        });
    });

    it('should show an error if the token validation fails', () => {
        service.getCurrentUser().subscribe((res: any) => {
            expect(res.status).toEqual(400);
        });
    });

    it('should show an error if the request does not have a token associated ', () => {
        service.getCurrentUser().subscribe((res: any) => {
            expect(res.status).toEqual(403);
        });
    });

    it('should show an error if the token expired', () => {
        service.getCurrentUser().subscribe((res: any) => {
            expect(res.status).toEqual(440);
        });
    });*/
});

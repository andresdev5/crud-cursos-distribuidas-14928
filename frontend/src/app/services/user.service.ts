import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserModel } from '@app/models';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    public constructor(private client: HttpClient) {}

    public getAll(): Observable<UserModel[]> {
        return this.client.get<UserModel[]>(`${environment.api.urls.serviceUsers}/users`);
    }

    public getById(id: string): Observable<UserModel> {
        return this.client.get<UserModel>(`${environment.api.urls.serviceUsers}/users/${id}`);
    }

    public create(user: UserModel): Observable<UserModel> {
        return this.client.post<UserModel>(`${environment.api.urls.serviceUsers}/users`, user);
    }

    public update(user: UserModel): Observable<UserModel> {
        return this.client.put<UserModel>(`${environment.api.urls.serviceUsers}/users`, user);
    }

    public delete(id: number): Observable<void> {
        return this.client.delete<void>(`${environment.api.urls.serviceUsers}/users/${id}`);
    }

    public search(query: string): Observable<UserModel[]> {
        return this.client.get<UserModel[]>(`${environment.api.urls.serviceUsers}/users/search`, {
            params: {
                query,
            },
        });
    }
}

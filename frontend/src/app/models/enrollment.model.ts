import { CourseModel } from './course.model';
import { EnrollmentStatus } from './enrollment-status.model';
import { UserModel } from './user.model';

export interface EnrollmentModel {
    id: number;
    course: CourseModel;
    user: UserModel;
    status: EnrollmentStatus;
    enrolledAt: Date;
}

export interface EnrollmentCreateModel {
    courseId: number;
    userIds: number[];
}

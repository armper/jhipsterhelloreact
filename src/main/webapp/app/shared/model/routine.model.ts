import dayjs from 'dayjs';
import { IExcercise } from 'app/shared/model/excercise.model';
import { IUser } from 'app/shared/model/user.model';

export interface IRoutine {
  id?: number;
  name?: string | null;
  dateStarted?: string | null;
  dateEnded?: string | null;
  goalDate?: string | null;
  startingBodyWeight?: number | null;
  endingBodyWeight?: number | null;
  excercises?: IExcercise[] | null;
  users?: IUser[] | null;
}

export const defaultValue: Readonly<IRoutine> = {};

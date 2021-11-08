import { IExcercise } from 'app/shared/model/excercise.model';

export interface ICycle {
  id?: number;
  reps?: number | null;
  volume?: number | null;
  excercise?: IExcercise | null;
}

export const defaultValue: Readonly<ICycle> = {};

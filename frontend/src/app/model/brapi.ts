
export interface BrapiResult<T> {
    result: T;
}

interface BrapiData<T> {
    data: T[];
}

export interface BrapiResults<T> {
    result: BrapiData<T>;
}


interface BrapiData<T> {
    data: T[];
}

export interface BrapiResults<T> {
    metadata: BrapiMetaData;
    result: BrapiData<T>;
}

export interface BrapiMetaData {

    pagination: {
        pageSize: number;
        currentPage: number;
        totalCount: number;
        totalPages: number;
    };

}

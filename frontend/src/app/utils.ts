export function asArray(obj) {
    if (!obj) {
        return null;
    }
    if (Array.isArray(obj)) {
        return obj;
    }
    return [obj];
}

export function arrayToString(arr: string[], sep: string) {
    return arr.join(sep);
}

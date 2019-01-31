export function asArray(obj) {
    if (!obj) {
        return null;
    }
    if (Array.isArray(obj)) {
        return obj;
    }
    return [obj];
}

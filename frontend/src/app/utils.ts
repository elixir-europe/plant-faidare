export function asArray<T>(obj: T | T[]): T[] {
    if (!obj) {
        return null;
    }
    if (Array.isArray(obj)) {
        return obj;
    }
    return [obj];
}

export interface KeyValueObject {
    key: string;
    value: string;
}

/**
 * Transform an object with string keys and values to a list of `KeyValueObject`.
 * Also makes sure the keys and values are truthy.
 */
export function toKeyValueObjects(object: Record<string, string>): KeyValueObject[] {
    return Object.entries(object)
        .filter(([key, value]) => !!key && !!value)
        .map(([key, value]) => ({ key, value }));
}


/**
 * Takes an object copies non null and non undefined field/value into a new object
 */
export function removeNullUndefined(obj: Record<string, any>): Record<string, any> {
    return Object.entries(obj)
        .filter(([key, value]) => value !== undefined && value !== null)
        .reduce((newObject, [key, value]) => {
            newObject[key] = value;
            return newObject;
        }, {});
}

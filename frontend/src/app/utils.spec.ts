import { KeyValueObject, removeNullUndefined, toKeyValueObjects } from './utils';


describe('utils.toKeyValueObjects', () => {

    it('should convert JS object to array of KeyValueObject', () => {
        const actual = toKeyValueObjects({
            'a': '1',
            'b': '2',
            'c': null,
            'd': '',
            '': 'e',
            'f': '3'
        });

        const expected: KeyValueObject[] = [
            { key: 'a', value: '1' },
            { key: 'b', value: '2' },
            { key: 'f', value: '3' },
        ];

        expect(actual).toEqual(expected);
    });

});

describe('utils.removeNullUndefined', () => {

    it('should remove undefined fields in object', () => {
        const input = {
            'a': undefined,
            'b': 3,
            'c': null,
        };
        const expected = {
            'b': 3
        };
        const actual = removeNullUndefined(input);
        expect(actual).toEqual(expected);
    });

});

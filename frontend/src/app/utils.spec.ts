import { KeyValueObject } from './utils';


describe('KeyValueObject', () => {

    it('should convert JS object to array of KeyValueObject', () => {
        const actual = KeyValueObject.fromObject({
            'a': '1',
            'b': '2',
            'c': null,
            'd': '',
            '': 'e',
            'f': '3'
        });

        const expected = [
            new KeyValueObject('a', '1'),
            new KeyValueObject('b', '2'),
            new KeyValueObject('f', '3'),
        ];

        expect(actual).toEqual(expected);
    });

});

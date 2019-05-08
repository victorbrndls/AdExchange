export default class AdTextParser {
    constructor() {
        this._result = '';
        this._pos = 0;

        this._options = {
            bold: false,
            italic: false
        };

        this.initializeConverterMap();
    }

    setText(text) {
        this._text = text;
    }

    initializeConverterMap() {
        this._converterMap = {
            '*': this.handleBold,
            '_': this.handleItalic,
            'default': this.handleDefault
        };
    }

    convertToHTML() {
        this.appendToResult('<span>');

        let textLength = this._text.length;

        while (this._pos < textLength) {
            let char = this._text[this._pos];

            let converter = this._converterMap[char] || this._converterMap['default'];
            converter.bind(this, char)();

            this._pos++;
        }

        this.appendToResult('</span>');

        console.log(this._result);
        return this._result;
    }

    handleDefault(char) {
        this.appendToResult(char);
    }

    handleBold(char) {
        this.handleSimpleTag(char, '*', 'b', 'bold');
    }

    handleItalic(char) {
        this.handleSimpleTag(char, '_', 'i', 'italic');
    }

    handleSimpleTag(currentChar, targetedChar, tag, name) {
        if (currentChar === targetedChar && this.getCharAt(this._pos + 1) === targetedChar) {
            this._options[name] ? this.appendToResult(`</${tag}>`) : this.appendToResult(`<${tag}>`);
            this._options[name] = !this._options[name];

            this._pos++;
        } else {
            this.appendToResult(targetedChar);
        }
    }

    getCharAt(pos) {
        return this._text[pos];
    }

    appendToResult(append) {
        this._result += append;
    }
}


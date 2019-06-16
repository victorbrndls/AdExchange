const TextAds = [
    {
        text: "Anúncio de texto, voce pode alterar **o estilo** do texto nos __campos abaixo__.",
        parsedOutput: [
            {
                tag: 'span',
                content: 'Anúncio de texto, voce pode alterar '
            }, {
                tag: 'b',
                content: 'o estilo '
            }, {
                tag: 'span',
                content: 'do texto nos '
            }, {
                tag: 'i',
                content: 'campos abaixo.'
            }
        ]
        ,
        bgColor: "#fff",
        textColor: "#000",
        textAlignment: "LEFT",
        textSize: 16,
    },
    {
        text: "Faça anúncios em vídeo online e tenha uma vantagem sobre os concorrentes. Use aa plataforma do software Biteable para transformar suas ideias de publicidade em vídeos que impulsionam negócios.",
        parsedOutput: [{
            "tag": "span",
            "content": "Faça anúncios em vídeo online e tenha uma vantagem sobre os concorrentes. "
        }, {"tag": "b", "content": "Use aa plataforma do software Biteable"}, {
            "tag": "span",
            "content": " para transformar suas ideias de publicidade em vídeos que impulsionam negócios."
        }],
        bgColor: "#fcf",
        textColor: "#10f",
        textAlignment: "LEFT",
        textSize: 16,
    },
    {
        text: "Crie aanúncios de vídeo gratuitos em um piscar de olhos. Eu não tinha nenhuma experiência em criação de vídeo e consegui produzir facilmente ",
        parsedOutput: [{
            "tag": "span",
            "content": "Crie aanúncios de vídeo gratuitos em um piscar de olhos. "
        }, {
            "tag": "i",
            "content": "Eu não tinha nenhuma experiência em criação de vídeo e consegui produzir facilmente "
        }],
        bgColor: "#00ff21",
        textColor: "#9c0012",
        textAlignment: "LEFT",
        textSize: 18,
    },
    {
        text: "Crie aanúncios de vídeo gratuitos em um piscar de olhos. Eu não tinha nenhuma experiência em criação de vídeo e consegui produzir facilmente ",
        parsedOutput: [{
            "tag": "span",
            "content": "Crie aanúncios de vídeo gratuitos em um piscar de olhos. "
        }, {
            "tag": "i",
            "content": "Eu não tinha nenhuma experiência em criação de vídeo e consegui produzir facilmente "
        }],
        bgColor: "#ff7f00",
        textColor: "#007d9c",
        textAlignment: "LEFT",
        textSize: 18,
    }
];

const ImageAds = [
    {
        imageUrl: "https://i.imgur.com/SQFA7oF.jpg"
    }, {
        imageUrl: "https://images.unsplash.com/photo-1558980663-3685c1d673c4?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
    }, {
        imageUrl: "https://images.unsplash.com/photo-1560568082-c15188aa6510?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
    }, {
        imageUrl: "https://images.unsplash.com/photo-1560568082-c15188aa6510?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
    }, {
        imageUrl: "https://images.unsplash.com/photo-1560568082-c15188aa6510?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
    }, {
        imageUrl: "https://images.unsplash.com/photo-1560568082-c15188aa6510?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
    }, {
        imageUrl: "https://images.unsplash.com/photo-1560568082-c15188aa6510?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"
    }
];


export default {
    TextAds,
    ImageAds
}
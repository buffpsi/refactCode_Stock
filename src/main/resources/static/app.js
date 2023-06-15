let stompClient = null;
let fromId = 0;
let ChatMessageUl = null;

function getChatMessages() {
    console.log("fromId : " + fromId);
    fetch(`/rooms/${chatRoomId}/messages?fromId=${fromId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }})
        .then(response => response.json())
        .then(body => {
            drawMessages(body);
        });
}

function drawMessages(messages) {
    if (messages.length > 0) {
        fromId = messages[messages.length - 1].message_id;
    }

    messages.forEach((message) => {

        const newItem = document.createElement("li");
        newItem.textContent = `${message.sender.username} : ${message.content}`;

        ChatMessageUl.appendChild(newItem);
    });
}


function ChatWriteMessage(form) {

    form.content.value = form.content.value.trim();

    stompClient.send(`/app/chats/${chatRoomId}/sendMessage`, {}, JSON.stringify({content: form.content.value}));

    form.content.value = '';
    form.content.focus();
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    const headers = {
        'X-CSRF-TOKEN': token,
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {
            getChatMessages();
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    connect();
});



/*
let stompClient = null;
let multiChatRoomId = 'multi'; // 다중 채팅방 ID
let oneToOneChatRoomId = 'one-to-one'; // 1대1 채팅방 ID
let fromId = 0;
let multiChatMessageUl = null;
let oneToOneChatMessageUl = null;

function getMultiChatMessages() {
    console.log("Multi Chat - fromId : " + fromId);
    fetch(`/rooms/${multiChatRoomId}/messages?fromId=${fromId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }})
        .then(response => response.json())
        .then(body => {
            drawMultiChatMessages(body);
        });
}

function getOneToOneChatMessages() {
    console.log("One-to-One Chat - fromId : " + fromId);
    fetch(`/rooms/${oneToOneChatRoomId}/messages?fromId=${fromId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }})
        .then(response => response.json())
        .then(body => {
            drawOneToOneChatMessages(body);
        });
}

function drawMultiChatMessages(messages) {
    if (messages.length > 0) {
        fromId = messages[messages.length - 1].message_id;
    }

    messages.forEach((message) => {
        const newItem = document.createElement("li");
        newItem.textContent = `${message.sender.username} : ${message.content}`;

        multiChatMessageUl.appendChild(newItem);
    });
}

function drawOneToOneChatMessages(messages) {
    if (messages.length > 0) {
        fromId = messages[messages.length - 1].message_id;
    }

    messages.forEach((message) => {
        const newItem = document.createElement("li");
        newItem.textContent = `${message.sender.username} : ${message.content}`;

        oneToOneChatMessageUl.appendChild(newItem);
    });
}

function sendMultiChatMessage(content) {
    const message = {
        content: content
    };

    stompClient.send(`/app/chats/${multiChatRoomId}/sendMessage`, {}, JSON.stringify(message));
}

function sendOneToOneChatMessage(receiverId, content) {
    const message = {
        receiverId: receiverId,
        content: content
    };

    stompClient.send(`/app/chats/${oneToOneChatRoomId}/sendMessage`, {}, JSON.stringify(message));
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    const headers = {
        'X-CSRF-TOKEN': token,
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe(`/topic/chats/${multiChatRoomId}`, function (data) {
            getMultiChatMessages();
        });

        stompClient.subscribe(`/topic/chats/${oneToOneChatRoomId}`, function (data) {
            getOneToOneChatMessages();
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    multiChatMessageUl = document.querySelector('.multi-chat__message-ul');
    oneToOneChatMessageUl = document.querySelector('.one-to-one-chat__message-ul');
    getMultiChatMessages();
    getOneToOneChatMessages();
    connect();
});*/

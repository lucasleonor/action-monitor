let messagingTo;
let stompClient;
let messageMap = new Map();
let user = Cookies.get('user');

$(document).ready(function () {
    disableMessaging();
    $('#username').html(user);
    console.log("connecting to chat...")
    let socket = new SockJS('/private/register');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/private/users", function (response) {
            console.log('updating users');
            let data = JSON.parse(response.body);
            let userList = $('#users');
            userList.empty();
            let messagingToConnected = false;
            for (let name of data) {
                if (name !== user) userList.append(userTemplate(name));
                messagingToConnected |= name === messagingTo;
            }
            if (!messagingToConnected) {
                disableMessaging();
            }
        });
        stompClient.send("/private/register", {}, "{}");
        stompClient.subscribe("/private/user/queue/specific-user/" + user, function (response) {
            console.log('receiving message');
            let msg = JSON.parse(response.body);
            msg.dateTime = new Date(msg.dateTime);
            let from = msg.from;
            let messages = messageMap.get(from);
            if (!messages) {
                messages = [];
                messageMap.set(from, messages);
            }
            messages.push(msg);
            if (from === messagingTo) {
                appendMessage(msg, true);
            }
        });
    });
});

function sendMsg() {
    let messageInput = $('#messageInput');
    let text = messageInput.val().trim();
    if (text !== '') {
        messageInput.val('');
        let message = {
            to: messagingTo,
            text: text,
            dateTime: new Date()
        };
        console.log('Sending message:' + message);
        stompClient.send("/private/chat", {}, JSON.stringify(message));
        appendMessage(message, false);
    }
}

function changeChat(username) {
    console.log('Messaging: ' + username);
    messagingTo = username;
    $('#form').show();
    let messages = messageMap.get(username);
    if (messages) {
        $('messages').empty();
        for (let message of messages) {
            appendMessage(message, message.from === messagingTo);
        }
    }
}

function appendMessage(message, incoming) {
    $('#messages').append(messageTemplate(message, incoming));
}

function disableMessaging() {
    messagingTo = null;
    $('#form').hide();
}

// ========== html templates =============

function messageTemplate(message, incoming) {
    return `<li>
                <div class="message-data ${incoming ? '' : 'text-right'}">
                    <span class="message-data-time">${$.format.toBrowserTimeZone(message.dateTime, 'dd/MM/yyyy HH:mm')}</span>
                </div>
                <div class="message ${incoming ? 'other-message' : 'my-message float-right'}">
                    ${message.text}
                </div>
            </li>`;
}

function userTemplate(username) {
    return `<div class="row p-2 userLabel" onclick="changeChat(\`${username}\`)">
                <div class="col-4" style="max-height: 3rem">
                    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/1024px-Circle-icons-profile.svg.png" alt="profile pic" class="h-100">
                </div>
                <div class="col-8 my-auto">
                    <h4 class="m-0">${username}</h4>
                </div>
            </div>`;
}
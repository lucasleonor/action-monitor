function userTemplate(username) {
    return `<a href="#" class="font-weight-bold text-light">
    <div class="row border border-light">
        <div class="col-4" style="max-height:3rem">
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/1024px-Circle-icons-profile.svg.png" alt="profile pic" class="h-100">
        </div>
        <div class="col-8">
            <h4>${username}</h4>
        </div>
    </div>
</a>`;
}

$(document).ready(function () {
    console.log("connecting to chat...")
    let socket = new SockJS('/private/register');
    let usersClient = Stomp.over(socket);
    usersClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        usersClient.subscribe("/private/users", function (response) {
            let data = JSON.parse(response.body);
            let userList = $('#users');
            userList.empty();
            for (let name of data) {
                console.log(name);
                userList.append(userTemplate(name));
            }
        });
        usersClient.send("/private/register", {}, "{}");
    });
});

function sendMsg(from, text) {
    stompClient.send("/private/history", {}, JSON.stringify({
        fromLogin: from,
        message: text
    }));
}
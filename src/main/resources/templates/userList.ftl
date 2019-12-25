
<#import "parts/common.ftl" as c>

<@c.page>
    <h>List of users</h>

    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">Name</th>
            <th scope="col">E-mail</th>
            <th scope="col">Role</th>
            <th scope="col">Edit</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <th class="info " scope="col">${user.username}</th>
                <th class="info " scope="col">${user.email}</th>
                <th><#list user.roles as role>${role}<#sep>, </#list></th>
                <th><a href="/user/${user.id}">edit</a></th>
            </tr>
        </#list>
        </tbody>
    </table>

    </tbody>
    </table>
</@c.page>
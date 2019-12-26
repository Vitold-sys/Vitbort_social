<#import "parts/common.ftl" as c>

<@c.page>
    <p>Here your can change your personal information</p>
    <h5>${username}</h5>
    ${message?ifExists}
    <form method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="Password" />
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email:</label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" placeholder="some@some.com" value="${email!''}" />
            </div>
        </div>
        <div class="form-group row">
            <div class="input-group">
                <label class="col-sm-2 col-form-label">Gender:</label>
                <div class="col-sm-6">
                    <select name="gender" autofocus class="form-control input-lg" id="gender">
                        <option>Male</option>
                        <option>Female</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Phone Number:</label>
            <div class="col-sm-6">
                <input type="phoneNumber" name="phoneNumber" value="<#if user??>${user.phoneNumber}</#if>"
                       class="form-control placeholder=" +375000000000" />
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Save</button>
    </form>
</@c.page>
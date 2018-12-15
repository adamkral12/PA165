import {IDepartment} from "../../types/Department";
import * as React from "react";
import {MouseEvent} from 'react';
import {defineAbility} from "../../config/ability";

interface IProps {
    department: IDepartment;
    onStartEdit: (event: MouseEvent<HTMLButtonElement>) => void;
}
export const DepartmentShowRow: React.FunctionComponent<IProps> = (props) => {
    const {latitude, specialization, longitude, country, city} = props.department;
    return <tr>
        <td>{city}</td>
        <td>{country}</td>
        <td>{latitude}</td>
        <td>{longitude}</td>
        <td>{specialization}</td>
        <td>{defineAbility().can('edit', 'Department') && <button onClick={props.onStartEdit} className={'btn btn-success'}>Edit</button>}</td>
    </tr>
};
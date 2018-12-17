export interface ISelectOption {
    readonly label: string;
    readonly value: string;
}

export const mapOptionsForSelect = (options: string[]): ISelectOption[] => {
    return options.map((language: string) =>
        ({value: language, label: language})
    );
};

export const mapValuesFromSelect = (selectOptions: ISelectOption[]): string[] => {
    return selectOptions.map((selectOption: ISelectOption) => (
        selectOption.value
    ))
};